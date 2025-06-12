package com.example.chat.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.chat.dto.request.ChatMessageRequestDto;
import com.example.chat.dto.request.ChatRoomRequestDto;
import com.example.chat.dto.request.CreateChatRoomRequestDto;
import com.example.chat.dto.request.JoinRequestDto;
import com.example.chat.dto.response.ChatRoomResponseDto;
import com.example.chat.dto.response.UserInfoResponseDto;
import com.example.chat.entity.ChatLog;
import com.example.chat.entity.ChatRoom;
import com.example.chat.entity.ChatRoomUser;
import com.example.chat.enums.ClubRole;
import com.example.chat.enums.MessageType;
import com.example.chat.enums.RoomType;
import com.example.chat.pubsub.RedisPublisher;
import com.example.chat.repository.ChatLogRepository;
import com.example.chat.repository.ChatRoomRepository;
import com.example.chat.repository.ChatRoomUserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import com.example.notification.event.ChatNotificationEvent;
import com.example.notification.event.ChatNotificationProducer;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
	private final ChatRoomRepository chatRoomRepository;
	private final ChatLogRepository chatLogRepository;
	private final ChatRoomUserRepository userRepository;
	// private final GetInfoFromExternal getInfoFromExternal;
	// private final SimpMessagingTemplate messagingTemplate;
	private final DummyInfoExternal dummyInfoExternal; // 무시해라 레빗아... 더미다
	private final ChatNotificationProducer notificationProducer;
	private final RedisPublisher redisPublisher;
	private final ObjectMapper objectMapper;

	/**
	 * Creates and persists a new chat room using the provided request data.
	 *
	 * @param request contains club ID, room name, creator user ID, image URL, and room type for the new chat room
	 */
	// CreateChatRoomRequestDto에 메서드 만들어서 이 코드 넣으면 서비스 코드 길이 더 줄어들 것 같아요.
	@Override
	public void createChatRoom(CreateChatRoomRequestDto request) {
		// 클럽 멤버 쪽 API 완료 시 요청 하는 구조로 정보를 받아오고 조건 검사 진행 예정
		// if (request.getRole()
		// 	== ClubRole.MEMBER) { // 외부 API지만 같은 회사내 다른 서버 API를 호출하는거임. 나중에 지정한 경로에서만 접속 가능하도록 WebConfig, SocketConfig 변경 필요
		// 	throw new ResponseStatusException(HttpStatus.FORBIDDEN, "해당 클럽에 가입되어 있지 않습니다.");
		// }

		ChatRoom room = new ChatRoom(
			request.getClubId(),
			request.getName(),
			request.getUserId(),
			request.getImageUrl(),
			request.getType());
		chatRoomRepository.save(room);
	}

	/**
	 * 설명: 채팅방 리스트 조회
	 * <p>
	 * 클럽 Id와 유저의 클럽내에서의 Role 기반으로 채팅방 리스트를 가져옴
	 * 외부 API 요청(도메인쪽으로)하여 Role 정보를 불러와서 갱신 해주고 불러옴
	 *
	 * @param clubId  : 채팅방 리스트가 속한 클럽 Id
	 * @param request : 유저 Id와 유저의 클럽내에서의 role 정보
	 * @return 채팅방 리스트 반환
	 * @author Tcimel
	 */
	@Override
	@Transactional
	public List<ChatRoomResponseDto> findChatRoomByClubAndRole(Long clubId, ChatRoomRequestDto request) {
		// 역할 조회
		UserInfoResponseDto userInfo = dummyInfoExternal.getUserInfo(request.getUserId());
		ClubRole userRole = ClubRole.valueOf(userInfo.getRole().toUpperCase());
		List<ChatRoomUser> users = userRepository.findByUserId(request.getUserId());

		for (ChatRoomUser u : users) {
			if (u.getClubRole() != userRole) {
				u.updateClubRole(userRole);
			}
		}
		userRepository.saveAll(users);

		List<ChatRoom> chatRooms;
		if (userRole == ClubRole.MANAGER || userRole == ClubRole.LEADER) {
			chatRooms = chatRoomRepository.findByClubId(clubId);
		} else {
			chatRooms = chatRoomRepository.findByClubIdAndType(clubId, RoomType.MEMBER);
		}

		return chatRooms.stream()
			.map(ChatRoomResponseDto::from)
			.collect(Collectors.toList());
	}

	/**
	 * Broadcasts a chat message to all subscribers of the specified chat room and saves the message to persistent storage.
	 * <p>
	 * The message is only broadcast if the user is a member of the chat room. The sender's nickname is set before broadcasting.
	 *
	 * @param request the chat message request containing room ID, user ID, message content, and type
	 */
	@Override
	@Transactional
	public void broadcastMessage(ChatMessageRequestDto request) {
		if (!userRepository.existsByRoomIdAndUserId(request.getRoomId(), request.getUserId()))
			return;

		/*저장 실패 후 이미 브로커로 전송된 메시지를 되돌릴 방법이 없습니다.
		트랜잭션 또는 try-catch 후 롤백 전략, 혹은 먼저 저장 후 전송하는 방식을 고려하세요.
		권한 미충족 시 return; 으로 무음 처리 → 클라이언트는 성공으로 오인할 수 있습니다. 명시적 에러 응답·에러 메시지 전송이 필요합니다.*/
		ChatRoomUser user = userRepository.findByRoomIdAndUserId(request.getRoomId(), request.getUserId())
			.orElseThrow(() -> new ResponseStatusException(
				HttpStatus.BAD_REQUEST, "잘못된 요청입니다."));
		request.setNickname(user.getNickname());

		/*String roomId = "/sub/chat/room/" + request.getRoomId();
		messagingTemplate.convertAndSend(roomId, request); // 브로드 캐스트
		saveMessage(request);*/

		List<ChatRoomUser> roomUsers = userRepository.findByRoomId(request.getRoomId());
		for (ChatRoomUser receiver : roomUsers) {
			if (!receiver.getUserId().equals(request.getUserId())) {
				ChatNotificationEvent event = new ChatNotificationEvent(
					receiver.getUserId(),
					request.getRoomId(),
					request.getNickname() + "님의 새 메시지가 도착하였습니다."
				);
				notificationProducer.send(event);
			}
		}
		/*String roomId = "/sub/chat/room/" + request.getRoomId();
		messagingTemplate.convertAndSend(roomId, request); // 브로드 캐스트*/

		try {
			System.out.println("🥕🥕🥕 Redis publish 실행");
			String json = objectMapper.writeValueAsString(request);
			redisPublisher.publish("room:" + request.getRoomId(), json);
		} catch (JsonProcessingException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "메세지 직렬화 실패");
		}

		// saveMessage(request);
	}

	/**
	 * Persists a chat message to the chat log repository.
	 *
	 * @param request the chat message data to be saved
	 */
	@Override
	public void saveMessage(ChatMessageRequestDto request) {
		ChatLog message = new ChatLog(
			request.getRoomId(),
			request.getUserId(),
			request.getNickname(),
			request.getMessage(),
			request.getType(),
			LocalDateTime.now()
		);
		chatLogRepository.save(message);
	}

	/**
	 * Retrieves all chat messages for the specified chat room, ordered by creation time in ascending order.
	 *
	 * @param roomId the ID of the chat room
	 * @return a list of chat logs for the room, sorted by creation time
	 */
	@Override
	@Transactional(readOnly = true)
	public List<ChatLog> getMessages(Long roomId) {
		return chatLogRepository.findByRoomIdOrderByCreatedAtAsc(roomId);
	}

	/**
	 * Adds a user to all accessible chat rooms in a club based on their role, creating membership records and broadcasting entry messages.
	 * <p>
	 * For each chat room the user is eligible to join and not already a member of, creates a new `ChatRoomUser` entry and sends an "ENTER" message to the room. After joining, refreshes the user's chat room list.
	 *
	 * @param clubId  the ID of the club whose chat rooms are being joined
	 * @param request contains the user ID of the joining user
	 */
	@Override
	@Transactional
	public void join(Long clubId, JoinRequestDto request) {
		List<ChatRoom> allRooms = chatRoomRepository.findByClubId(clubId);
		Long userId = request.getUserId();

		// 외부 API에서 닉네임, 역할 조회
		// UserInfoResponseDto userInfo = getInfoFromExternal.getUserInfo(userId);
		UserInfoResponseDto userInfo = dummyInfoExternal.getUserInfo(userId);
		ClubRole userRole = ClubRole.valueOf(userInfo.getRole().toUpperCase());

		List<ChatRoom> accessibleRooms = allRooms.stream()
			.filter(room -> {
				if (room.getType() == RoomType.MANAGER) {
					// 룸 타입이 MANAGER인 경우 → 유저 롤이 MANAGER 또는 LEADER면 OK
					return userRole == ClubRole.LEADER || userRole == ClubRole.MANAGER;
				}
				return true;
			}).toList();

		// 이미 가입한 방 제외하고 join 진행
		for (ChatRoom room : accessibleRooms) {
			boolean alreadyJoined = userRepository.existsByRoomIdAndUserId(room.getId(), userId);
			if (!alreadyJoined) {
				ChatRoomUser join = new ChatRoomUser(
					clubId,
					room.getId(),
					userId,
					userInfo.getNickname(),
					userRole,
					LocalDateTime.now()
				);
				userRepository.save(join);
				ChatMessageRequestDto enterMessage = ChatMessageRequestDto.from(MessageType.ENTER, room.getId(), userId,
					userInfo.getNickname(),
					userInfo.getNickname() + "님이 입장하셨습니다.");
				broadcastMessage(enterMessage);
			}
		}

		// 가입 후 채팅방 목록 불러오기
		ChatRoomRequestDto chatRoomRequest = ChatRoomRequestDto.from(userId, userRole);
		findChatRoomByClubAndRole(clubId, chatRoomRequest);
	}

	/**
	 * Removes a user from all chat rooms in a club that they have access to based on their role, and broadcasts a leave message to each room.
	 *
	 * @param clubId the ID of the club from which the user is leaving chat rooms
	 * @param userId the ID of the user leaving the chat rooms
	 */
	@Override
	@Transactional
	public void leave(Long clubId, Long userId) {
		List<ChatRoom> allRooms = chatRoomRepository.findByClubId(clubId);

		// 외부 API에서 닉네임, 역할 조회
		// UserInfoResponseDto userInfo = getInfoFromExternal.getUserInfo(userId);
		UserInfoResponseDto userInfo = dummyInfoExternal.getUserInfo(userId);
		ClubRole userRole = ClubRole.valueOf(userInfo.getRole().toUpperCase());

		List<ChatRoom> accessibleRooms = allRooms.stream()
			.filter(room -> {
				if (room.getType() == RoomType.MANAGER) {
					return userRole == ClubRole.LEADER || userRole == ClubRole.MANAGER;
				}
				return true;
			}).toList();

		// 방 탈퇴 진행
		for (ChatRoom room : accessibleRooms) {
			userRepository.deleteByRoomIdAndUserId(room.getId(), userId);
			ChatMessageRequestDto leaveMessage = ChatMessageRequestDto.from(MessageType.LEAVE, room.getId(), userId,
				userInfo.getNickname(),
				userInfo.getNickname() + "님이 퇴장하셨습니다.");
			broadcastMessage(leaveMessage);
		}
	}

	@Override
	public List<ChatRoomUser> getUserInRoom(Long roomId) {
		return userRepository.findByRoomId(roomId);
	}

}
