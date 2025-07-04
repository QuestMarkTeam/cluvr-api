package com.example.cluvrapi.domain.tilReview.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WeeklyDateRange {
	private final LocalDateTime startDateTime;
	private final LocalDateTime endDateTime;
}
