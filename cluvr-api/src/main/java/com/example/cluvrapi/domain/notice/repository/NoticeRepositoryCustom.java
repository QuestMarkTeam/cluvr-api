package com.example.cluvrapi.domain.notice.repository;

import org.springframework.data.domain.Pageable;

import com.example.cluvrapi.domain.common.dto.PageResponseDto;
import com.example.cluvrapi.domain.notice.dto.response.InfoNoticeResponseDto;

public interface NoticeRepositoryCustom {

	/**
	 * 설명: 공지사항 단건 조회하는 쿼리문
	 *
	 * <p>{Dto Projection 적용}
	 *
	 * @param clubId   {설명: 클럽 고유 식별자}
	 * @param noticeId {설명: 공지사항 고유 식별자}
	 * @return InfoNoticeResponseDto {공지사항 정보}
	 * @author {sinyoung0403}
	 */
	InfoNoticeResponseDto findNoticeById(Long clubId, Long noticeId);

	/**
	 * 설명: 공지사항 다건 조회하는 쿼리문
	 *
	 * <p>{Dto Projection 적용, 페이징 처리}
	 *
	 * @param clubId   {설명: 클럽 고유 식별자}
	 * @param pageable {설명: 페이징 객체}
	 * @return PageResponseDto<InfoNoticeResponseDto> {공지사항 정보 페이징 객체}
	 * @author {sinyoung0403}
	 */
	PageResponseDto<InfoNoticeResponseDto> findAllNotice(Long clubId, Pageable pageable);
}
