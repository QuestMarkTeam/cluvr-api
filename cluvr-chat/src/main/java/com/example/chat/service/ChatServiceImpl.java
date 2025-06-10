package com.example.chat.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import com.example.chat.repository.ChatLogRepository;
import com.example.chat.repository.ChatRoomRepository;
import com.example.chat.repository.ChatRoomUserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
	private final ChatRoomRepository chatRoomRepository;
	private final ChatLogRepository chatLogRepository;
	private final ChatRoomUserRepository userRepository;
	// private final GetInfoFromExternal getInfoFromExternal;
	private final SimpMessagingTemplate messagingTemplate;
	private final DummyInfoExternal dummyInfoExternal;

	/**
	 * Creates and persists a new chat room using the provided request data.
	 *
	 * @param request contains club ID, room name, creator user ID, image URL, and room type for the new chat room
	 */
	@Override
	public void createChatRoom(CreateChatRoomRequestDto request) {
		ChatRoom room = new ChatRoom(
			request.getClubId(),
			request.getName(),
			request.getUserId(),
			request.getImageUrl(),
			request.getType());
		chatRoomRepository.save(room);
	}

	/****
	 * Retrieves a list of chat rooms in a club accessible to a user based on their role.
	 *
	 * Fetches the user's current role from an external service and updates their role in all chat room memberships if changed.
	 * Returns all chat rooms for users with the MANAGER or LEADER role; otherwise, returns only MEMBER-type chat rooms.
	 *
	 * @param clubId the ID of the club whose chat rooms are being queried
	 * @param request contains the user ID and their role information within the club
	 * @return a list of chat room response DTOs accessible to the user
	 */
	@Override
	public List<ChatRoomResponseDto> findChatRoomByClubAndRole(Long clubId, ChatRoomRequestDto request) {
		// 역할 조회
		UserInfoResponseDto userInfo = dummyInfoExternal.getUserInfo(request.getUserId());
		ClubRole userRole = ClubRole.valueOf(userInfo.getRole().toUpperCase());
		List<ChatRoomUser> users = userRepository.findByUserId(request.getUserId());

		for (ChatRoomUser u : users) {
			if (u.getClubRole() != userRole) {
				u.setClubRole(userRole);
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
	 *
	 * The message is only broadcast if the user is a member of the chat room. The sender's nickname is set before broadcasting.
	 *
	 * @param request the chat message request containing room ID, user ID, message content, and type
	 */
	@Override
	@Transactional
	public void broadcastMessage(ChatMessageRequestDto request) {
		if (!userRepository.existsByRoomIdAndUserId(request.getRoomId(), request.getUserId()))
			return;

		ChatRoomUser user = userRepository.findByRoomIdAndUserId(request.getRoomId(), request.getUserId());
		request.setNickname(user.getNickname());

		String roomId = "/sub/chat/room/" + request.getRoomId();
		messagingTemplate.convertAndSend(roomId, request); // 브로드 캐스트
		saveMessage(request);
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
	public List<ChatLog> getMessages(Long roomId) {
		return chatLogRepository.findByRoomIdOrderByCreatedAtAsc(roomId);
	}

	/**
	 * Adds a user to all accessible chat rooms in a club based on their role, creating membership records and broadcasting entry messages.
	 *
	 * For each chat room the user is eligible to join and not already a member of, creates a new `ChatRoomUser` entry and sends an "ENTER" message to the room. After joining, refreshes the user's chat room list.
	 *
	 * @param clubId the ID of the club whose chat rooms are being joined
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

	/**
	 * Retrieves the list of users currently present in the specified chat room.
	 *
	 * @param roomId the ID of the chat room
	 * @return a list of ChatRoomUser entities representing users in the room
	 */
	@Override
	public List<ChatRoomUser> getUserInRoom(Long roomId) {
		return userRepository.findByRoomId(roomId);
	}

}
