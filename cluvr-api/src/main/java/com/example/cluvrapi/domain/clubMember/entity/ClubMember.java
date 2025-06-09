package com.example.cluvrapi.domain.clubMember.entity;

import static jakarta.persistence.GenerationType.*;

import com.example.cluvrapi.domain.club.entity.Club;
import com.example.cluvrapi.domain.clubMember.entity.enums.MemberStatus;
import com.example.cluvrapi.domain.common.entity.BaseTimeEntity;
import com.example.cluvrapi.domain.user.entity.User;

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
	private MemberStatus memberStatus;

	public ClubMember(Club club, User user, MemberStatus memberStatus) {
		this.club = club;
		this.user = user;
		this.memberStatus = memberStatus;
	}

}
