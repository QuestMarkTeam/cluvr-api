package com.example.cluvrapi.domain.club.dto.response;

import java.time.LocalDateTime;

import lombok.Getter;

import com.example.cluvrapi.domain.club.enums.ClubType;
import com.querydsl.core.annotations.QueryProjection;

/**
 * 클럽 단건 조회 응답 DTO
 *
 * <p> 단일 클럽의 상세 정보를 포함하며, 클럽 상세 조회 API 의 응답으로 사용됩니다.
 * 포함 필드:
 * - name: 클럽 이름
 * - clubType: 클럽 타입 (예: 공개/비공개)
 * - categoryDetail: 클럽 카테고리 상세명 (문자열)
 * - greeting: 클럽 소개말
 * - description: 클럽 상세 설명
 * - postUrl: 클럽 포스터 이미지 URL
 * - createAt: 클럽 생성 일시
 *
 * @author sinyoung0403
 */

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
