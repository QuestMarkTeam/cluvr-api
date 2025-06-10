package com.example.chat.dto.request;

import lombok.Getter;

@Getter
public class JoinRequestDto {
	private Long userId;

	/**
		 * Constructs a new JoinRequestDto with the specified user ID.
		 *
		 * @param userId the ID of the user making the join request
		 */
	public JoinRequestDto(Long userId) {
		this.userId = userId;
	}

	/**
	 * Creates a new JoinRequestDto instance with the specified user ID.
	 *
	 * @param userId the ID of the user to include in the join request
	 * @return a new JoinRequestDto containing the given user ID
	 */
	public static JoinRequestDto from(Long userId) {
		return new JoinRequestDto(userId);
	}
}
