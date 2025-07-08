package com.example.cluvrapi.domain.reaction.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.example.cluvrapi.domain.reaction.enums.ReactionType;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReactionStatusResponseDto {
	private boolean hasReaction;
	private ReactionType reactionType;
	
	public static ReactionStatusResponseDto of(boolean hasReaction, ReactionType reactionType) {
		return new ReactionStatusResponseDto(hasReaction, reactionType);
	}
	
	public static ReactionStatusResponseDto noReaction() {
		return new ReactionStatusResponseDto(false, null);
	}
} 