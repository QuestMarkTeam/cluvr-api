package com.example.cluvrapi.domain.club.dto.response;

import java.time.LocalDateTime;

import lombok.Getter;

import com.example.cluvrapi.domain.club.entity.Club;
import com.example.cluvrapi.domain.club.enums.ClubType;
import com.example.cluvrapi.domain.club.enums.JoinType;

/**
 * 내 클럽 목록 조회 응답 DTO
 *
 * <p> 사용자가 가입한 클럽들의 기본 정보를 담아 반환합니다.
 *
 * @author sinyoung0403
 */

@Getter
public class MyClubResponseDto {
	private Long clubId;
	private String name;
	private ClubType clubType;
	private String greeting;
	private String description;
	private String posterUrl;
	private Boolean isPublic;
	private JoinType joinType;
	private int maxMemberCount;
	private int minCloverRequirement;
	private LocalDateTime createdAt;

	public MyClubResponseDto(Club club) {
		this.clubId = club.getId();
		this.name = club.getName();
		this.clubType = club.getClubType();
		this.greeting = club.getGreeting();
		this.description = club.getDescription();
		this.posterUrl = club.getPosterUrl();
		this.isPublic = club.getIsPublic();
		this.joinType = club.getJoinType();
		this.maxMemberCount = club.getMaxMemberCount();
		this.minCloverRequirement = club.getMinCloverRequirement();
		this.createdAt = club.getCreatedAt();
	}

	/**
	 * Club 엔티티로부터 MyClubResponseDto를 생성합니다.
	 *
	 * @param club Club 엔티티
	 * @return MyClubResponseDto 인스턴스
	 */
	public static MyClubResponseDto from(Club club) {
		return new MyClubResponseDto(club);
	}
} 