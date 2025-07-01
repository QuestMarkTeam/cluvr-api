package com.example.cluvrapi.domain.join.service.handler;

import java.util.concurrent.TimeUnit;

import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;

import org.redisson.api.RLock;
import org.springframework.stereotype.Service;

import com.example.cluvrapi.domain.club.entity.Club;
import com.example.cluvrapi.domain.clubMember.entity.ClubMember;
import com.example.cluvrapi.domain.clubMember.entity.enums.ClubMemberRole;
import com.example.cluvrapi.domain.clubMember.entity.enums.ClubMemberStatus;
import com.example.cluvrapi.domain.clubMember.repository.ClubMemberRepository;
import com.example.cluvrapi.domain.join.service.JoinRedisService;
import com.example.cluvrapi.domain.join.validator.ClubJoinValidator;
import com.example.cluvrapi.domain.user.entity.User;

@RequiredArgsConstructor
@Service
public class DirectJoinHandler {

	private final JoinRedisService redissonClient;
	private final ClubMemberRepository clubMemberRepository;
	private final ClubJoinValidator clubJoinValidator;

	@Transactional(rollbackOn = Exception.class)
	public void handle(Club club, User user) {
		RLock writeLock = redissonClient.getWriteLock(club.getId());

		int maxRetries = 3;
		long baseDelayMs = 100;
		boolean acquired = false;

		for (int i = 0; i < maxRetries; i++) {
			try {
				acquired = writeLock.tryLock(1, 5, TimeUnit.SECONDS);
				if (acquired)
					break;
				Thread.sleep(baseDelayMs * (1L << i));
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				throw new RuntimeException("락 획득 중 인터럽트됨", e);
			}
		}

		if (!acquired) {
			throw new RuntimeException("클럽 락 획득 실패 (clubId=" + club.getId() + ")");
		}

		try {
			clubJoinValidator.validateNotFullMemberCount(club);
			ClubMember member = new ClubMember(club, user, ClubMemberRole.MEMBER, ClubMemberStatus.ACTIVE);
			clubMemberRepository.save(member);
		} finally {
			if (writeLock.isHeldByCurrentThread()) {
				writeLock.unlock();
			}
		}
	}
}

