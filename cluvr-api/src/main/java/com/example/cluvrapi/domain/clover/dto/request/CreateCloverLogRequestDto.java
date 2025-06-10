package com.example.cluvrapi.domain.clover.dto.request;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class CreateCloverLogRequestDto {
	private Integer amount;
	private LocalDateTime createdAt;
	private LocalDateTime deletedAt;
	private String description;
	private Long userId;
}
