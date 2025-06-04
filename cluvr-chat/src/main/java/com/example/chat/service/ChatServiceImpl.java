package com.example.chat.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.chat.dto.request.ChatMessageRequestDto;
import com.example.chat.dto.request.ChatRoomRequestDto;
import com.example.chat.dto.request.CreateChatRoomRequestDto;
import com.example.chat.dto.response.ChatRoomResponseDto;
import com.example.chat.dto.response.UserInfoResponseDto;
import com.example.chat.entity.ChatLog;
import com.example.chat.entity.ChatRoom;
import com.example.chat.entity.ChatRoomUser;
import com.example.chat.enums.ClubRole;
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
	private final GetInfoFromExternal getInfoFromExternal;

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
		List<ChatRoom> chatRooms;

		if (request.getRole() == ClubRole.MANAGER || request.getRole() == ClubRole.LEADER) {
			chatRooms = chatRoomRepository.findByClubId(clubId);
		} else {
			chatRooms = chatRoomRepository.findByClubIdAndType(clubId, RoomType.MEMBER);
		}

		return chatRooms.stream()
			.map(ChatRoomResponseDto::from)
			.collect(Collectors.toList());
	}

	@Override
	public void saveMessage(ChatMessageRequestDto request) {
		ChatLog message = new ChatLog(
			request.getRoomId(),
			request.getUserId(),
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
	public void join(Long clubId, Long userId) {
		List<ChatRoom> allRooms = chatRoomRepository.findByClubId(clubId);

		// 외부 API에서 닉네임, 역할 조회
		UserInfoResponseDto userInfo = getInfoFromExternal.getUserInfo(userId);
		String userRole = userInfo.getRole();
		ClubRole clubRole = ClubRole.valueOf(userRole.toUpperCase());

		List<ChatRoom> accessibleRooms = allRooms.stream()
			.filter(room -> {
				if (room.getType() == RoomType.MANAGER) {
					return clubRole.equals("MANAGER") || clubRole.equals("LEADER");
				}
				return true;
			}).toList();

		// 이미 가입한 방 제외
		for (ChatRoom room : accessibleRooms) {
			boolean alreadyJoined = userRepository.existsByRoomIdAndUserId(room.getId(), userId);
			if (!alreadyJoined) {
				ChatRoomUser join = new ChatRoomUser(
					clubId,
					room.getId(),
					userId,
					userInfo.getNickname(),
					clubRole,
					LocalDateTime.now()
				);
				userRepository.save(join);
			}
		}
	}

	@Override
	public List<ChatRoomUser> getUserInRoom(Long roomId) {
		return userRepository.findByRoomId(roomId);
	}
}
