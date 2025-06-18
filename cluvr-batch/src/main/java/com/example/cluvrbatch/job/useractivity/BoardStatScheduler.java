package com.example.cluvrbatch.job.useractivity;

import lombok.RequiredArgsConstructor;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BoardStatScheduler {

	private final JobLauncher jobLauncher;
	private final Job boardLogJob;
	private final BoardStatJobService boardStatJobService;

	@Scheduled(cron = "0 * * * * *") // 매일 새벽 3시
	public void runBoardStatJob() {
		try {
			boardStatJobService.runJob();
		} catch (Exception e) {
			// 로깅 or 슬랙 알림
			throw new IllegalStateException("runBoardStatJob 실행 실패", e);
		}
	}
}
