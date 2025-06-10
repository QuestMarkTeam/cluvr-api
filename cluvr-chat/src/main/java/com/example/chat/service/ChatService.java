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
	/****
 * Creates a new chat room using the provided request data.
 *
 * @param request the details required to create the chat room
 */
void createChatRoom(CreateChatRoomRequestDto request);

	/****
 * Retrieves chat rooms within a specified club filtered by user role criteria.
 *
 * @param clubId the unique identifier of the club
 * @param request the criteria for filtering chat rooms by user role
 * @return a list of chat rooms matching the club and role criteria
 */
List<ChatRoomResponseDto> findChatRoomByClubAndRole(Long clubId, ChatRoomRequestDto request);

	/**
 * Sends a chat message to all participants in the specified chat room.
 *
 * @param request the chat message details to broadcast
 */
void broadcastMessage(ChatMessageRequestDto request);

	/****
 * Persists a chat message to storage based on the provided request data.
 *
 * @param request the chat message details to be saved
 */
void saveMessage(ChatMessageRequestDto request);

	/****
 * Retrieves the list of chat messages for the specified chat room.
 *
 * @param roomId the unique identifier of the chat room
 * @return a list of chat log entries for the given room
 */
List<ChatLog> getMessages(Long roomId);

	/****
 * Adds a user to a chat room within the specified club.
 *
 * @param clubId the identifier of the club
 * @param userId the user join request containing user details
 */
void join(Long clubId, JoinRequestDto userId);

	/****
 * Removes a user from a chat room within the specified club.
 *
 * @param clubId the identifier of the club containing the chat room
 * @param userId the identifier of the user to remove
 */
void leave(Long clubId, Long userId);

	/****
 * Retrieves the list of users currently present in the specified chat room.
 *
 * @param roomId the unique identifier of the chat room
 * @return a list of users in the chat room
 */
List<ChatRoomUser> getUserInRoom(Long roomId);
}
