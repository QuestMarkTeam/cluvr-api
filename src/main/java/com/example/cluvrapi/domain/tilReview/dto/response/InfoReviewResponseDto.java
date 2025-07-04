package com.example.cluvrapi.domain.tilReview.dto.response;

import lombok.Getter;

import com.querydsl.core.annotations.QueryProjection;

@Getter
public class InfoReviewResponseDto {
	private Long reviewId;
	private Long clubId;
	private Long tilId;
	private String tilContent;
	private Boolean reviewed;
	private int score;
	private String summary;
	private String feedback;

	@QueryProjection
	public InfoReviewResponseDto(Long reviewId, Long clubId, Long tilId, String tilContent, Boolean reviewed, int score,
		String summary, String feedback) {
		this.reviewId = reviewId;
		this.clubId = clubId;
		this.tilId = tilId;
		this.tilContent = tilContent;
		this.reviewed = reviewed;
		this.score = score;
		this.summary = summary;
		this.feedback = feedback;
	}
}
