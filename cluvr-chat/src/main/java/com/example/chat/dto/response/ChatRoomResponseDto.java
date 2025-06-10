package com.example.chat.dto.response;

import com.example.chat.entity.ChatRoom;
import com.example.chat.enums.RoomType;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatRoomResponseDto {
	private Long id;
	private Long clubId;
	private String name;
	private RoomType type;

	/**
	 * Constructs a ChatRoomResponseDto with the specified id, clubId, name, and type.
	 *
	 * @param id the unique identifier of the chat room
	 * @param clubId the identifier of the associated club
	 * @param name the name of the chat room
	 * @param type the type of the chat room
	 */
	public ChatRoomResponseDto(Long id, Long clubId, String name, RoomType type) {
		this.id = id;
		this.clubId = clubId;
		this.name = name;
		this.type = type;
	}

	/**
	 * Creates a ChatRoomResponseDto from a ChatRoom entity.
	 *
	 * @param room the ChatRoom entity to convert
	 * @return a new ChatRoomResponseDto containing the room's id, clubId, name, and type
	 */
	public static ChatRoomResponseDto from(ChatRoom room) {
		return new ChatRoomResponseDto(
			room.getId(),
			room.getClubId(),
			room.getName(),
			room.getType()
		);
	}
}
