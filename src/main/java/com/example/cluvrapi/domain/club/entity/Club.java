package com.example.cluvrapi.domain.club.entity;

import jakarta.persistence.Column;
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

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.example.cluvrapi.domain.club.enums.ClubType;
import com.example.cluvrapi.domain.club.enums.JoinType;
import com.example.cluvrapi.domain.common.entity.BaseTimeEntity;
import com.example.cluvrapi.global.exception.BusinessException;
import com.example.cluvrapi.global.response.ResponseCode;

/**
 * 클럽 도메인 엔티티입니다.
 *
 * <p>사용자는 클럽을 생성하고, 다양한 클럽에 가입할 수 있습니다.
 * 클럽은 아래와 같은 속성을 가집니다:
 * <ul>
 *   <li>이름(name)</li>
 *   <li>소개(intro)</li>
 *   <li>설명(description)</li>
 *   <li>클럽 타입(clubType)</li>
 *   <li>제한 인원수(limitMemberCount)</li>
 *   <li>현재 인원수(currentMemberCount)</li>
 *   <li>삭제 여부(isDeleted)</li>
 *   <li>초대 코드(inviteCode)</li>
 * </ul>
 *
 * <p> 기본적으로 {@link BaseTimeEntity}를 상속받아 생성 및 수정 시간 정보를 포함합니다.
 */

@Entity
@Getter
@Table(name = "clubs")
@SQLDelete(sql = "UPDATE clubs SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Club extends BaseTimeEntity {

	private static final int MAX_MEMBER_LIMIT = 50;

	/**
	 * 고유 식별자
	 */

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * 클럽명
	 */

	@Column(unique = true, nullable = false, length = 20)
	private String name;

	/**
	 * 클럽 타입:
	 * STUDY, PROJECT,	COMMUNITY
	 */

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ClubType clubType;

	/**
	 * 최대 인원수
	 */

	@Column(nullable = false)
	private int maxMemberCount = 30;

	/**
	 * Clover 제한
	 */

	@Column(nullable = false)
	private int minCloverRequirement = 0;

	/**
	 * 소개말
	 */

	@Column(nullable = false, length = 100)
	private String greeting;

	/**
	 * 자세한 설명
	 */

	@Column(nullable = false, length = 255)
	private String description;

	/**
	 * 이미지 URL
	 */

	@Column(nullable = false)
	private String posterUrl;

	/**
	 * 공개 여부
	 */

	@Column(nullable = false)
	private Boolean isPublic = true;

	/**
	 * SoftDeleted
	 * 삭제 여부
	 */

	@Column(name = "is_deleted", nullable = false)
	private Boolean isDeleted = false;

	/**
	 * 가입 방식:
	 * PROBLEM_FORM,       	// 문제 양식 제출
	 * SUBMISSION_FORM,    	// 가입 신청서 제출
	 * DIRECT_JOIN,        	// 가입 신청과 동시에 바로 가입
	 * SIMPLE_REQUEST,     	// 단순 가입 신청
	 * INVITE_CODE 			// 초대코드 가입 -> 클럽 생성 시 스스로 선택할 수 없다. (비공개 클럽 시 해당 클럽가입양식 선택)
	 */

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private JoinType joinType;

	/**
	 * 기본 생성자
	 */

	public Club(String name, ClubType clubType, int maxMemberCount, int minCloverRequirement, String greeting,
		String description, String posterUrl, Boolean isPublic, JoinType joinType) {
		this.name = name;
		this.clubType = clubType;
		this.maxMemberCount = maxMemberCount;
		this.minCloverRequirement = minCloverRequirement;
		this.greeting = greeting;
		this.description = description;
		this.posterUrl = posterUrl;
		this.isPublic = isPublic;
		this.joinType = joinType;
	}

	/**
	 * 설명: 클럽명 수정
	 *
	 * @param name 클럽명
	 * @author sinyoung0403
	 */

	public void updateName(String name) {
		this.name = name;
	}

	/**
	 * 설명: 소개말 수정
	 *
	 * @param greeting 소개말
	 * @author sinyoung0403
	 */

	public void updateGreeting(String greeting) {
		this.greeting = greeting;
	}

	/**
	 * 설명: 자세한 설명 수정
	 *
	 * @param description 자세한 설명
	 * @author sinyoung0403
	 */

	public void updateDescription(String description) {
		this.description = description;
	}

	/**
	 * 설명: 인원수 증가
	 *
	 * <p> 가입 인원이 최소 1명에서 최대 50명까지 가능하다.
	 *
	 * @param increment 증가되는 인원수
	 * @author sinyoung0403
	 */

	public void upgradeMemberCount(int increment) {
		if (increment <= 0) {
			throw new BusinessException(ResponseCode.INVALID_REQUEST, "추가 인원은 1명 이상이어야 합니다.");
		}

		if (this.maxMemberCount + increment > MAX_MEMBER_LIMIT) {
			throw new BusinessException(ResponseCode.INVALID_REQUEST, "최대 50명을 초과할 수 없습니다.");
		}

		this.maxMemberCount += increment;
	}

	/**
	 * 설명: 클럽의 가입 방식을 변경
	 *
	 * @param joinType 변경할 가입 방식
	 * @author sinyoung0403
	 */

	public void updateJoinType(JoinType joinType) {
		if (joinType == null) {
			throw new BusinessException(ResponseCode.INVALID_REQUEST, "joinType 은 null 일 수 없습니다.");
		}

		this.joinType = joinType;
	}

	/**
	 * 설명: 클럽의 공개 여부를 변경
	 *
	 * @param isPublic 변경할 공개 여부 (true: 공개, false: 비공개)
	 * @author sinyoung0403
	 */

	public void updatePrivacy(Boolean isPublic) {
		if (isPublic == null) {
			throw new BusinessException(ResponseCode.INVALID_REQUEST, "isPublic 은 null 일 수 없습니다.");
		}

		this.isPublic = isPublic;
	}

	public void delete() {
		this.isDeleted = Boolean.TRUE;
	}
}
