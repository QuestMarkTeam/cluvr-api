package com.example.cluvrapi.domain.club.dto.response;

import lombok.Getter;

import com.example.cluvrapi.domain.category.enums.CategoryType;
import com.example.cluvrapi.domain.club.enums.ClubType;
import com.querydsl.core.annotations.QueryProjection;

/**
 * 클럽 리스트 조회 응답 DTO
 *
 * <p> 클럽 다건 조회 시 각 클럽의 주요 정보를 담아 반환합니다.
 * 페이징 처리나 QueryDSL 사용 시 반환 타입으로 활용됩니다.
 * <p>
 * 포함 필드:
 * - clubId: 클럽 고유 식별자
 * - authorId: 클럽 생성자(작성자) ID
 * - name: 클럽 이름
 * - clubType: 클럽 타입 (예: 공개/비공개)
 * - categoryDetail: 클럽 카테고리 상세
 * - greeting: 클럽 소개말
 * - postUrl: 클럽 포스터 이미지 URL
 * - maxMemberCounter: 클럽 최대 인원 수
 *
 * @author sinyoung0403
 */

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
	private int minCloverRequirement;

	@QueryProjection
	public FindAllClubResponseDto(Long clubId, Long authorId, String name, ClubType clubType,
		CategoryType categoryDetail,
		String greeting, String postUrl, int maxMemberCounter, int minCloverRequirement) {
		this.clubId = clubId;
		this.authorId = authorId;
		this.name = name;
		this.clubType = clubType;
		this.categoryDetail = categoryDetail;
		this.greeting = greeting;
		this.postUrl = postUrl;
		this.maxMemberCounter = maxMemberCounter;
		this.minCloverRequirement = minCloverRequirement;
	}
}
