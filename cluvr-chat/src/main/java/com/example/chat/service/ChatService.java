package com.example.chat.service;

import java.util.List;

import com.example.chat.dto.request.ChatMessageRequestDto;
import com.example.chat.dto.request.ChatRoomRequestDto;
import com.example.chat.dto.request.CreateChatRoomRequestDto;
import com.example.chat.dto.request.JoinRequestDto;
import com.example.chat.dto.response.ChatRoomResponseDto;
import com.example.chat.entity.ChatLog;
import com.example.chat.entity.ChatRoomUser;

public interface ChatService {
	void createChatRoom(CreateChatRoomRequestDto request);

	List<ChatRoomResponseDto> findChatRoomByClubAndRole(Long clubId, ChatRoomRequestDto request);

	void broadcastMessage(ChatMessageRequestDto request);

	void saveMessage(ChatMessageRequestDto request);

	List<ChatLog> getMessages(Long roomId);

	void join(Long clubId, JoinRequestDto request);

	void leave(Long clubId, Long userId);

	List<ChatRoomUser> getUserInRoom(Long roomId);
}
