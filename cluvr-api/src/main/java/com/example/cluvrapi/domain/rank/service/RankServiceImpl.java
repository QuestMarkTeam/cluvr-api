package com.example.cluvrapi.domain.rank.service;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.cluvrapi.domain.rank.dto.FindRankLogResponseDto;
import com.example.cluvrapi.domain.rank.dto.FindRankResponseDto;
import com.example.cluvrapi.domain.rank.dto.request.CreateRankLogRequestDto;
import com.example.cluvrapi.domain.rank.dto.request.CreateRankRequestDto;
import com.example.cluvrapi.domain.rank.dto.request.UpdateRankRequestDto;
import com.example.cluvrapi.domain.rank.entity.Rank;
import com.example.cluvrapi.domain.rank.entity.RankLog;
import com.example.cluvrapi.domain.rank.repository.RankLogRepository;
import com.example.cluvrapi.domain.rank.repository.RankRepository;
import com.example.cluvrapi.domain.user.entity.User;
import com.example.cluvrapi.domain.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class RankServiceImpl implements RankService {

	private final RankRepository rankRepository;
	private final UserRepository userRepository;
	private final RankLogRepository rankLogRepository;

	@Override
	@Transactional(readOnly=true)
	public FindRankResponseDto findRankByUserId(Long userId) {
		return rankRepository.findScoreByUserId(userId);
	}

	@Override
	public void deleteRank(Long rankId) {
		Rank rank = rankRepository.findByIdOrElseThrow(rankId);
		rankRepository.delete(rank);
	}

	@Transactional
	@Override
	public void createRank(CreateRankRequestDto requestDto) {
		User user = userRepository.findByIdOrElseThrow(requestDto.getUserId());
		Rank rank = new Rank(requestDto.getTier(), requestDto.getScore(), user);
		rankRepository.save(rank);
	}

	@Override
	public void updateRank(Long rankId, UpdateRankRequestDto requestDto) {
		Rank rank = rankRepository.findByIdOrElseThrow(rankId);
		rank.updateScore(requestDto.getScore());
	}

	@Override
	@Transactional(readOnly=true)
	public FindRankLogResponseDto findRankLogByUserId(Long userId) {
		return rankRepository.findRankLogByUserId(userId);
	}

	@Override
	public void createRankLog(CreateRankLogRequestDto requestDto) {
		User user = userRepository.findByIdOrElseThrow(requestDto.getUserId());
		RankLog rankLog = new RankLog(user, requestDto.getDescription(), requestDto.getAmount(),
			requestDto.getCreatedAt(), requestDto.getDeletedAt());
		rankLogRepository.save(rankLog);

	}

	@Override
	public void deleteRankLog(Long rankId) {
		RankLog rankLog = rankLogRepository.findByIdOrElseThrow(rankId);
		rankLogRepository.delete(rankLog);
	}
}
