package com.example.cluvrapi.domain.tilReview.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.example.cluvrapi.domain.common.entity.BaseTimeEntity;

@Entity
@Getter
@Table(name = "til_reviews")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TilReview extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Long requestId;

	@Column(nullable = false)
	private Long clubId;

	@Column(nullable = false)
	private Long tilId;

	@Column(columnDefinition = "TEXT")
	private String tilContent;

	private Boolean reviewed = false;

	private int score;
	private String summary;
	private String feedback;

	public TilReview(Long requestId, Long clubId, Long tilId, String tilContent) {
		this.requestId = requestId;
		this.clubId = clubId;
		this.tilId = tilId;
		this.tilContent = tilContent;
	}
}
