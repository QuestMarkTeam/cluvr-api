package com.example.cluvrapi.domain.club.entity;

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

import com.example.cluvrapi.domain.club.enums.ClubType;
import com.example.cluvrapi.domain.club.enums.JoinType;
import com.example.cluvrapi.domain.common.entity.BaseTimeEntity;
import com.example.cluvrapi.domain.user.entity.User;

@Entity
@Getter
@Table(name = "clubs")
@SQLDelete(sql = "UPDATE clubs SET is_deleted = true WHERE id = ?")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Club extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 20)
	private String name;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ClubType clubType;

	@Column(nullable = false)
	private int maxMemberCount = 30;

	@Column(nullable = false)
	private int minScoreRequirement = 0;

	@Column(nullable = false, length = 100)
	private String greeting;

	@Column(nullable = false, length = 255)
	private String description;

	@Column(nullable = false)
	private String posterUrl;

	@Column(nullable = false)
	private Boolean isPublic = true;

	@Column(nullable = false)
	private Boolean isDeleted = false;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private JoinType joinType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	public Club(String name, ClubType clubType, int maxMemberCount, int minScoreRequirement,
		String greeting, String description, String posterUrl, Boolean isPublic, JoinType joinType, User user) {
		this.name = name;
		this.clubType = clubType;
		this.maxMemberCount = maxMemberCount;
		this.minScoreRequirement = minScoreRequirement;
		this.greeting = greeting;
		this.description = description;
		this.posterUrl = posterUrl;
		this.isPublic = isPublic;
		this.joinType = joinType;
		this.user = user;
	}

	public void updateName(String name) {
		this.name = name;
	}

	public void updateGreeting(String greeting) {
		this.greeting = greeting;
	}

	public void updateDescription(String description) {
		this.description = description;
	}

	/**
	 * 설명: 인원수 증가
	 *
	 * @param increment
	 * @author sinyoung0403
	 */
	public void upgradeMemberCount(int increment) {
		if (increment <= 0) {
			throw new IllegalArgumentException("추가 인원은 1명 이상이어야 합니다.");
		}

		if (this.maxMemberCount + increment > 50) {
			throw new IllegalStateException("최대 50명을 초과할 수 없습니다.");
		}

		this.maxMemberCount += increment;
	}

	public void changeJoinTypeToSimpleRequest() {
		this.joinType = JoinType.SIMPLE_REQUEST;
	}
}
