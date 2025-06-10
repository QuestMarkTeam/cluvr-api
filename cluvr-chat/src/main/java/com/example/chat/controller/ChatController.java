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

	/**
	 * Handles a request for a user to join a chat room within a specified club.
	 *
	 * @param clubId the ID of the club whose chat room is being joined
	 * @param request the join request details
	 * @return a response indicating the success of the join operation
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
	 * @param roomId the unique identifier of the chat room
	 * @return a response containing the list of users in the chat room
	 */
	@GetMapping("/room/{roomId}/users")
	public ResponseEntity<BaseResponse<List<ChatRoomUser>>> getUserInRoom(@PathVariable Long roomId) {
		List<ChatRoomUser> users = chatService.getUserInRoom(roomId);
		return ResponseEntity.ok(BaseResponse.success(users, ResponseCode.OK));
	}

	/**
	 * Handles incoming chat messages over WebSocket and broadcasts them to connected clients.
	 *
	 * The message is also persisted for chat history.
	 *
	 * @param request the chat message payload to broadcast and store
	 */
	@MessageMapping("/message") // /pub/chat/message
	public void sendMessage(@Payload ChatMessageRequestDto request) {
		chatService.broadcastMessage(request); // 채팅 로그 몽고디비에 저장
	}

	/****
	 * Retrieves the list of chat messages for a specific chat room within a club.
	 *
	 * @param clubId the identifier of the club containing the chat room
	 * @param roomId the identifier of the chat room whose messages are to be retrieved
	 * @return a response entity containing a list of chat logs for the specified room
	 */

	@GetMapping("/club/{clubId}/chat/{roomId}")
	public ResponseEntity<BaseResponse<List<ChatLog>>> getMessages(
		@PathVariable Long clubId,
		@PathVariable Long roomId
	) {
		List<ChatLog> messages = chatService.getMessages(roomId);
		return ResponseEntity.ok(BaseResponse.success(messages, ResponseCode.OK));
	}

	/****
	 * Creates a new chat room if the requesting user has sufficient privileges.
	 *
	 * Only users with the role of club owner or manager are allowed to create chat rooms; members are denied access.
	 *
	 * @param request the chat room creation request containing club ID, user ID, and user role
	 * @return a response indicating success with ResponseCode.CREATED, or an error if access is denied
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

	/****
	 * Retrieves a list of chat rooms for a given club, filtered by the requesting user's role.
	 *
	 * Club owners and managers receive all chat rooms, while regular members do not see manager-type chat rooms.
	 * The request body must include user identification and role information.
	 * Uses POST to prevent URL manipulation by requiring request body data.
	 *
	 * @param clubId the ID of the club whose chat rooms are being requested
	 * @param request contains user ID and role information for filtering chat rooms
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
