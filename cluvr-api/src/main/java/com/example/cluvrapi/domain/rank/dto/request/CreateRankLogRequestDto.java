package com.example.cluvrapi.domain.rank.dto.request;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class CreateRankLogRequestDto {
	private Integer amount;
	private LocalDateTime createdAt;
	private LocalDateTime deletedAt;
	private String description;
	private Long userId;
}
