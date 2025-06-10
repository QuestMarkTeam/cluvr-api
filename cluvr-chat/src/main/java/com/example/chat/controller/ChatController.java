package com.example.chat.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.chat.dto.request.ChatMessageRequestDto;
import com.example.chat.dto.request.ChatRoomRequestDto;
import com.example.chat.dto.request.CreateChatRoomRequestDto;
import com.example.chat.dto.request.JoinRequestDto;
import com.example.chat.dto.response.ChatRoomResponseDto;
import com.example.chat.entity.ChatLog;
import com.example.chat.entity.ChatRoomUser;
import com.example.chat.enums.ClubRole;
import com.example.chat.service.ChatService;
import com.example.global.response.response.BaseResponse;
import com.example.global.response.response.ResponseCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {

	private final SimpMessagingTemplate messagingTemplate;
	private final ChatService chatService;

	/****
	 * Allows a user to join a chat room within a specified club.
	 *
	 * @param clubId the ID of the club containing the chat room
	 * @param request the join request details
	 * @return HTTP 200 response with a success code if the join operation is successful
	 */
	@PostMapping("club/{clubId}/chat/join")
	public ResponseEntity<BaseResponse<?>> joinChatRoom(
		@PathVariable Long clubId,
		@RequestBody JoinRequestDto request
	) {
		chatService.join(clubId, request);
		return ResponseEntity.ok(BaseResponse.success(ResponseCode.OK));
	}

	/**
	 * Retrieves the list of users currently present in the specified chat room.
	 *
	 * @param roomId the ID of the chat room
	 * @return a response containing the list of users in the chat room
	 */
	@GetMapping("/room/{roomId}/users")
	public ResponseEntity<BaseResponse<List<ChatRoomUser>>> getUserInRoom(@PathVariable Long roomId) {
		List<ChatRoomUser> users = chatService.getUserInRoom(roomId);
		return ResponseEntity.ok(BaseResponse.success(users, ResponseCode.OK));
	}

	/**
	 * Handles incoming chat messages sent via WebSocket and broadcasts them to chat participants.
	 *
	 * Receives a chat message payload, delegates broadcasting and persistence to the chat service, and does not return a response.
	 */
	@MessageMapping("/message") // /pub/chat/message
	public void sendMessage(@Payload ChatMessageRequestDto request) {
		chatService.broadcastMessage(request); // 채팅 로그 몽고디비에 저장
	}

	/**
	 * Retrieves chat messages for a specific chat room within a club.
	 *
	 * @param clubId the ID of the club containing the chat room
	 * @param roomId the ID of the chat room whose messages are to be retrieved
	 * @return a response containing the list of chat messages for the specified room
	 */

	@GetMapping("/club/{clubId}/chat/{roomId}")
	public ResponseEntity<BaseResponse<List<ChatLog>>> getMessages(
		@PathVariable Long clubId,
		@PathVariable Long roomId
	) {
		List<ChatLog> messages = chatService.getMessages(roomId);
		return ResponseEntity.ok(BaseResponse.success(messages, ResponseCode.OK));
	}

	/**
	 * Creates a new chat room if the requesting user has sufficient privileges.
	 *
	 * Only users with the role of club leader or manager are allowed to create chat rooms. If the user has the MEMBER role, an access denied error response is returned.
	 *
	 * @param request the chat room creation request containing club ID, user ID, and user role
	 * @return a response indicating success with ResponseCode.CREATED, or an error if the user lacks permission
	 */

	@PostMapping("/chat/create")
	public ResponseEntity<BaseResponse<?>> createChatRoom(
		@RequestBody CreateChatRoomRequestDto request) {
		log.info("방 생성 요청: {}", request);
		if (request.getRole() == ClubRole.MEMBER) {
			return ResponseEntity.ok(BaseResponse.error(ResponseCode.ACCESS_DENIED, "권한이 없습니다. 관리자에게 문의하세요."));
		}
		chatService.createChatRoom(request);
		return ResponseEntity.ok(BaseResponse.success(ResponseCode.CREATED));
	}

	/**
	 * Retrieves a list of chat rooms for a club, filtered by the user's role.
	 *
	 * Only club owners and managers can view all chat rooms, while regular members are restricted from seeing manager-only chat rooms.
	 *
	 * @param clubId the ID of the club whose chat rooms are being requested
	 * @param request contains user identification and role information for filtering chat rooms
	 * @return a response containing the list of chat rooms accessible to the user based on their role
	 */

	@PostMapping("/club/{clubId}/chat/list")
	public ResponseEntity<BaseResponse<List<ChatRoomResponseDto>>> getChatRooms(
		@PathVariable Long clubId,
		@RequestBody ChatRoomRequestDto request
	) {
		log.info("🥕🥕🥕 clubId = {}", clubId);
		log.info("🥕🥕🥕 request = {}", request);
		List<ChatRoomResponseDto> chatRooms = chatService.findChatRoomByClubAndRole(clubId, request);
		return ResponseEntity.ok(BaseResponse.success(chatRooms, ResponseCode.OK));
	}

}
