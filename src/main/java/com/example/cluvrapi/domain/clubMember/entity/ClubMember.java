package com.example.cluvrapi.domain.clubMember.entity;

import static jakarta.persistence.GenerationType.*;

import com.example.cluvrapi.domain.club.entity.Club;
import com.example.cluvrapi.domain.clubMember.entity.enums.ClubMemberRole;
import com.example.cluvrapi.domain.clubMember.entity.enums.ClubMemberStatus;
import com.example.cluvrapi.domain.common.entity.BaseTimeEntity;
import com.example.cluvrapi.domain.user.entity.User;
import com.example.cluvrapi.global.exception.BusinessException;
import com.example.cluvrapi.global.response.ResponseCode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "club_members")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClubMember extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "club_id", nullable = false)
	private Club club;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 10)
	private ClubMemberRole clubMemberRole;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 10)
	private ClubMemberStatus clubMemberStatus;

	public ClubMember(Club club, User user, ClubMemberRole clubMemberRole, ClubMemberStatus clubMemberStatus) {
		this.club = club;
		this.user = user;
		this.clubMemberRole = clubMemberRole;
		this.clubMemberStatus = clubMemberStatus;
	}

	public void rejoin() {
		this.clubMemberStatus = ClubMemberStatus.ACTIVE;
	}

	public void changeRole(ClubMemberRole newRole) {
		this.clubMemberRole = newRole;
	}

	public void withdraw() {
		if (this.clubMemberRole == ClubMemberRole.OWNER) {
			throw new BusinessException(ResponseCode.INVALID_REQUEST, "OWNER는 탈퇴할 수 없습니다.");
		}
		this.clubMemberStatus = ClubMemberStatus.WITHDRAWN;
	}

	public void kick() {
		if (this.clubMemberRole == ClubMemberRole.OWNER) {
			throw new BusinessException(ResponseCode.INVALID_REQUEST, "OWNER는 강퇴할 수 없습니다.");
		}
		this.clubMemberStatus = ClubMemberStatus.KICKED;
	}

}
