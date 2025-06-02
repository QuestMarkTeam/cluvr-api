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
		User findUser = userRepository.findByIdOrElseThrow(userId);

		Club findClub = clubRepository.findByIdOrElseThrow(clubId);

		Notice notice = new Notice(findUser, findClub, createNoticeRequestDto.getTitle(),
			createNoticeRequestDto.getContent());

		noticeRepository.save(notice);

		return CreateNoticeResponseDto.from(notice.getId());
	}

	@Override
	public InfoNoticeResponseDto findNoticeById(Long clubId, Long noticeId) {
		return noticeRepository.findNoticeById(clubId, noticeId);
	}

	@Override
	public PageResponseDto<InfoNoticeResponseDto> findAllNotice(Long clubId, Pageable pageable) {
		return noticeRepository.findAllNotice(clubId, pageable);
	}

	@Override
	@Transactional
	public void updateNotice(Long noticeId, UpdateNoticeRequestDto updateNoticeRequestDto) {
		Notice findNotice = noticeRepository.findByIdOrElseThrow(noticeId);

		if (updateNoticeRequestDto.getTitle() != null) {
			findNotice.updateTitle(updateNoticeRequestDto.getTitle());
		}

		if (updateNoticeRequestDto.getContent() != null) {
			findNotice.updateContent(updateNoticeRequestDto.getContent());
		}
	}

	@Override
	@Transactional
	public void deleteNotice(Long noticeId) {
		Notice findNotice = noticeRepository.findByIdOrElseThrow(noticeId);

		noticeRepository.delete(findNotice);
	}
}
