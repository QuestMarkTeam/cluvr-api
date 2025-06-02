package com.example.cluvrapi.domain.club.dto.response;

import java.time.LocalDateTime;

import lombok.Getter;

import com.example.cluvrapi.domain.club.enums.ClubType;
import com.querydsl.core.annotations.QueryProjection;

@Getter
public class FindClubResponseDto {
	private String name;
	private ClubType clubType;
	private String categoryDetail;
	private String greeting;
	private String description;
	private String postUrl;
	private LocalDateTime createAt;

	@QueryProjection
	public FindClubResponseDto(ClubType clubType, String name, String categoryDetail, String greeting,
		String description,
		String postUrl, LocalDateTime createAt) {
		this.name = name;
		this.clubType = clubType;
		this.categoryDetail = categoryDetail;
		this.greeting = greeting;
		this.description = description;
		this.postUrl = postUrl;
		this.createAt = createAt;
	}
}
