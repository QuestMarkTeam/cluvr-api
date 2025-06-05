package com.example.chat.entity;

import java.time.LocalDateTime;

import com.example.chat.enums.ClubRole;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "chat_room_user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomUser {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long clubId;
	private Long roomId;
	private Long userId;
	private String nickname;

	@Enumerated(EnumType.STRING)
	private ClubRole clubRole;

	private LocalDateTime joinedAt;

	public ChatRoomUser(Long clubId, Long roomId, Long userId, String nickname, ClubRole clubRole,
		LocalDateTime joinedAt) {
		this.clubId = clubId;
		this.roomId = roomId;
		this.userId = userId;
		this.nickname = nickname;
		this.clubRole = clubRole;
		this.joinedAt = joinedAt;
	}
}
