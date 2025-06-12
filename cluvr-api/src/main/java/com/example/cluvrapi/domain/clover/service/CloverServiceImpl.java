package com.example.cluvrapi.domain.clover.service;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.cluvrapi.domain.clover.dto.FindCloverLogResponseDto;
import com.example.cluvrapi.domain.clover.dto.FindCloverResponseDto;
import com.example.cluvrapi.domain.clover.dto.request.CreateCloverLogRequestDto;
import com.example.cluvrapi.domain.clover.dto.request.CreateCloverRequestDto;
import com.example.cluvrapi.domain.clover.dto.request.UpdateCloverRequestDto;
import com.example.cluvrapi.domain.clover.entity.Clover;
import com.example.cluvrapi.domain.clover.entity.CloverLog;
import com.example.cluvrapi.domain.clover.repository.CloverLogRepository;
import com.example.cluvrapi.domain.clover.repository.CloverRepository;
import com.example.cluvrapi.domain.notification.enums.NotiTargetType;
import com.example.cluvrapi.domain.notification.enums.NotificationType;
import com.example.cluvrapi.domain.notification.event.NotificationEvent;
import com.example.cluvrapi.domain.notification.event.NotificationProducer;
import com.example.cluvrapi.domain.user.entity.User;
import com.example.cluvrapi.domain.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class CloverServiceImpl implements CloverService {

	private final CloverRepository cloverRepository;
	private final UserRepository userRepository;
	private final CloverLogRepository cloverLogRepository;
	private final NotificationProducer notificationProducer;

	@Override
	@Transactional(readOnly = true)
	public FindCloverResponseDto findCloverByUserId(Long userId) {
		return cloverRepository.findScoreByUserId(userId);
	}

	@Transactional
	@Override
	public void deleteClover(Long cloverId) {
		Clover clover = cloverRepository.findByIdOrElseThrow(cloverId);
		cloverRepository.delete(clover);
	}

	@Transactional
	@Override
	public void createClover(CreateCloverRequestDto requestDto) {
		User user = userRepository.findByIdOrElseThrow(requestDto.getUserId());
		Clover clover = new Clover(requestDto.getTier(), requestDto.getScore(), user);
		cloverRepository.save(clover);
	}

	@Transactional
	@Override
	public void updateClover(Long cloverId, UpdateCloverRequestDto requestDto) {
		Clover clover = cloverRepository.findByIdOrElseThrow(cloverId);
		clover.updateScore(requestDto.getScore());

		Long receiverId = requestDto.getUser().getId();  // 랭크 → 유저
		String content = String.format("랭크 점수가 %d점으로 갱신되었습니다.", requestDto.getScore());

		NotificationEvent event = NotificationEvent.from(
			receiverId,
			NotificationType.RANK,         // type
			content,        // content
			NotiTargetType.USER,         // targetType
			receiverId          // targetId
		);

		notificationProducer.send(event);
	}

	@Override
	@Transactional(readOnly = true)
	public List<FindCloverLogResponseDto> findCloverLogByUserId(Long userId) {
		return cloverRepository.findCloverLogByUserId(userId);
	}

	@Override
	public void createCloverLog(CreateCloverLogRequestDto requestDto) {
		User user = userRepository.findByIdOrElseThrow(requestDto.getUserId());
		CloverLog cloverLog = new CloverLog(user, requestDto.getDescription(), requestDto.getAmount(),
			requestDto.getCreatedAt(), requestDto.getDeletedAt());
		cloverLogRepository.save(cloverLog);

	}

	@Override
	public void deleteCloverLog(Long cloverId) {
		CloverLog cloverLog = cloverLogRepository.findByIdOrElseThrow(cloverId);
		cloverLogRepository.delete(cloverLog);
	}
}
