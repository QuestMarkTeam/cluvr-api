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

import com.example.cluvrapi.domain.club.entity.Club;
import com.example.cluvrapi.domain.club.enums.JoinType;
import com.example.cluvrapi.domain.join.enums.JoinStatus;
import com.example.cluvrapi.domain.user.entity.User;

/**
 * 신청의 본체, 메타 정보를 담고 있다.
 */

@Entity
@Getter
@Table(name = "join_requests")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JoinRequest {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "club_id", nullable = false)
	private Club club;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private JoinStatus joinStatus;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private JoinType joinType;

	public JoinRequest(User user, Club club, JoinStatus joinStatus, JoinType joinType) {
		this.user = user;
		this.club = club;
		this.joinStatus = joinStatus;
		this.joinType = joinType;
	}
}
