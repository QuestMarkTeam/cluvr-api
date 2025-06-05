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

	@Override
	public List<ChatLog> getMessages(Long roomId) {
		return chatLogRepository.findByRoomIdOrderByCreatedAtAsc(roomId);
	}

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
