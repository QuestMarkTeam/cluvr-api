package com.example.chat.dto.request;

import com.example.chat.enums.ClubRole;
import com.example.chat.enums.RoomType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateChatRoomRequestDto {
	private Long clubId; // 클럽 ID (도메인 서버가 전달)
	private RoomType type; // 채팅방 타입(MANAGER, MEMBER)
	private Long userId; // 생성자 ID (도메인 서버가 전달)
	private ClubRole role; // 해당 유저의 클럽에서의 role 정보 (LEADER, MANAGER, MEMBER)
	private String name; // 채팅방 명
	private String imageUrl;
}
