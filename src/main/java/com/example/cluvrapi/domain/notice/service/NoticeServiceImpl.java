package com.example.cluvrapi.domain.notice.service;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.cluvrapi.domain.club.entity.Club;
import com.example.cluvrapi.domain.club.repository.ClubRepository;
import com.example.cluvrapi.domain.common.dto.PageResponseDto;
import com.example.cluvrapi.domain.notice.dto.reqeust.CreateNoticeRequestDto;
import com.example.cluvrapi.domain.notice.dto.reqeust.UpdateNoticeRequestDto;
import com.example.cluvrapi.domain.notice.dto.response.CreateNoticeResponseDto;
import com.example.cluvrapi.domain.notice.dto.response.InfoNoticeResponseDto;
import com.example.cluvrapi.domain.notice.entity.Notice;
import com.example.cluvrapi.domain.notice.repository.NoticeRepository;
import com.example.cluvrapi.domain.user.entity.User;
import com.example.cluvrapi.domain.user.repository.UserRepository;
import com.example.cluvrapi.global.exception.BusinessException;
import com.example.cluvrapi.global.response.ResponseCode;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

	private final UserRepository userRepository;
	private final ClubRepository clubRepository;
	private final NoticeRepository noticeRepository;

	@Override
	@Transactional
	public CreateNoticeResponseDto createNotice(Long userId, Long clubId,
		CreateNoticeRequestDto createNoticeRequestDto) {
		// 1) 유저 및 클럽 조회
		User findUser = userRepository.findByIdOrElseThrow(userId);
		Club findClub = clubRepository.findByIdOrElseThrow(clubId);

		// 2) Notice Entity 생성
		Notice notice = new Notice(findUser, findClub, createNoticeRequestDto.getTitle(),
			createNoticeRequestDto.getContent());

		// 3) notice 저장
		noticeRepository.save(notice);

		return CreateNoticeResponseDto.from(notice.getId());
	}

	@Override
	public InfoNoticeResponseDto findNoticeById(Long clubId, Long noticeId) {
		return noticeRepository.findNoticeDtoById(clubId, noticeId).orElseThrow(
			() -> new BusinessException(ResponseCode.NOT_FOUND, "존재하지 않는 공지사항 입니다.")
		);
	}

	@Override
	public PageResponseDto<InfoNoticeResponseDto> findAllNotice(Long clubId, Pageable pageable) {
		return noticeRepository.findAllNoticeDtoByClubId(clubId, pageable);
	}

	@Override
	@Transactional
	public void updateNotice(Long userId, Long clubId, Long noticeId, UpdateNoticeRequestDto updateNoticeRequestDto) {
		// 1) Notice 조회
		Notice findNotice = noticeRepository.findNoticeByIdAndClubId(clubId, noticeId).orElseThrow(
			() -> new BusinessException(ResponseCode.NOT_FOUND, "해당 데이터가 존재하지 않습니다.")
		);

		// 2) 수정
		if (updateNoticeRequestDto.getTitle() != null) {
			findNotice.updateTitle(updateNoticeRequestDto.getTitle());
		}

		if (updateNoticeRequestDto.getContent() != null) {
			findNotice.updateContent(updateNoticeRequestDto.getContent());
		}
	}

	@Override
	@Transactional
	public void deleteNotice(Long userId, Long clubId, Long noticeId) {
		// 1) Notice 조회
		Notice findNotice = noticeRepository.findNoticeByIdAndClubId(clubId, noticeId).orElseThrow(
			() -> new BusinessException(ResponseCode.NOT_FOUND, "해당 데이터가 존재하지 않습니다.")
		);

		// 2) 삭제
		noticeRepository.delete(findNotice);
	}
}
