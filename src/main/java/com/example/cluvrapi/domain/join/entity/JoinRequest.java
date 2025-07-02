package com.example.cluvrapi.domain.join.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.example.cluvrapi.domain.club.entity.Club;
import com.example.cluvrapi.domain.club.enums.JoinType;
import com.example.cluvrapi.domain.join.enums.JoinStatus;
import com.example.cluvrapi.domain.user.entity.User;

/**
 * 신청의 본체, 메타 정보를 담고 있습니다.
 */

@Entity
@Getter
@Table(name = "join_requests")
@SQLDelete(sql = "UPDATE join_requests SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JoinRequest {

	/**
	 * 식별자
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * 가입 신청을 한 유저
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	/**
	 * 가입 신청 대상인 클럽
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "club_id", nullable = false)
	private Club club;

	/**
	 * 가입 상태 값 :
	 * PENDING,    // 대기 (아직 승인되지 않음)
	 * APPROVED,   // 승인
	 * REJECTED,   // 거절
	 * CANCELED    // 신청자가 요청 취소
	 */
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private JoinStatus joinStatus;

	/**
	 * 가입 방식 :
	 * PROBLEM_FORM, 		// 문제 양식 제출
	 * SUBMISSION_FORM,   	// 가입 신청서 제출
	 * DIRECT_JOIN,        // 가입 신청과 동시에 바로 가입
	 * SIMPLE_REQUEST      // 단순 가입 신청
	 */
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private JoinType joinType;

	/**
	 * Soft Deleted
	 */
	@Column(nullable = false)
	private boolean isDeleted = false;

	/**
	 * 설명: 생성자 메서드
	 *
	 * @param user       {설명: 유저}
	 * @param club       {설명: 클럽}
	 * @param joinStatus {설명: 가입 신청 상태}
	 * @param joinType   {설명: 가입 방식}
	 * @return JoinRequest
	 * @author sinyoung0403
	 */
	public JoinRequest(User user, Club club, JoinStatus joinStatus, JoinType joinType) {
		this.user = user;
		this.club = club;
		this.joinStatus = joinStatus;
		this.joinType = joinType;
	}

	/**
	 * 설명: 가입 요청을 Soft Deleted 해주는 메서드
	 *
	 * @author sinyoung0403
	 */
	public void delete() {
		this.isDeleted = true;
	}

	/**
	 * 설명: 가입 요청을 approve 해주는 메서드
	 *
	 * @author sinyoung0403
	 */

	public void approve() {
		this.joinStatus = JoinStatus.APPROVED;
	}

	/**
	 * 설명: 가입 요청을 reject 해주는 메서드
	 *
	 * @author sinyoung0403
	 */

	public void reject() {
		this.joinStatus = JoinStatus.REJECTED;
	}
}
