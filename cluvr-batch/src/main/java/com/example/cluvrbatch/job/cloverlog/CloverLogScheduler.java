package com.example.cluvrbatch.job.cloverlog;

import lombok.RequiredArgsConstructor;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CloverLogScheduler {

	private final JobLauncher jobLauncher;
	private final Job cloverLogJob;

	@Scheduled(cron = "0 0 3 * * *") // 매일 새벽 3시
	public void runPointLogJob() {
		try {
			JobParameters params = new JobParametersBuilder()
				.addLong("timestamp", System.currentTimeMillis()) // 동일 JobInstance 방지
				.toJobParameters();

			jobLauncher.run(cloverLogJob, params);

		} catch (Exception e) {
			// 로깅 or 슬랙 알림
			throw new IllegalStateException("PointLogJob 실행 실패", e);
		}
	}
}
