package com.example.cluvrapi.domain.club.dto.response;

import lombok.Getter;

import com.example.cluvrapi.domain.category.enums.CategoryType;
import com.example.cluvrapi.domain.club.enums.ClubType;
import com.querydsl.core.annotations.QueryProjection;

@Getter
public class FindAllClubResponseDto {
	private Long clubId;
	private Long authorId;
	private String name;
	private ClubType clubType;
	private CategoryType categoryDetail;
	private String greeting;
	private String postUrl;
	private int maxMemberCounter;

	@QueryProjection
	public FindAllClubResponseDto(Long clubId, Long authorId, String name, ClubType clubType,
		CategoryType categoryDetail,
		String greeting, String postUrl, int maxMemberCounter) {
		this.clubId = clubId;
		this.authorId = authorId;
		this.name = name;
		this.clubType = clubType;
		this.categoryDetail = categoryDetail;
		this.greeting = greeting;
		this.postUrl = postUrl;
		this.maxMemberCounter = maxMemberCounter;
	}
}
