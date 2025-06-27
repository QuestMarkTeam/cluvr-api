package com.example.cluvrapi.domain.notice.repository;

import java.util.Optional;

import org.springframework.data.domain.Pageable;

import com.example.cluvrapi.domain.common.dto.PageResponseDto;
import com.example.cluvrapi.domain.notice.dto.response.InfoNoticeResponseDto;
import com.example.cluvrapi.domain.notice.entity.Notice;

public interface NoticeRepositoryQuery {

	/**
	 * 설명: 클럽 ID와 공지사항 ID로 공지사항 엔티티를 단건 조회하는 쿼리문
	 *
	 * <p> 내부 로직용으로 사용되며, 영속성 컨텍스트를 직접 다루는 경우에 사용됩니다.
	 *
	 * @param clubId   {설명: 클럽 고유 식별자}
	 * @param noticeId {설명: 공지사항 고유 식별자}
	 * @return Optional<Notice> {설명: 조회된 공지사항 엔티티}
	 * @author sinyoung0403
	 */
	Optional<InfoNoticeResponseDto> findNoticeById(Long clubId, Long noticeId);

	Optional<Notice> findNoticeByIdAndClubId(Long clubId, Long noticeId);

	/**
	 * 설명: 클럽 ID와 공지사항 ID로 공지사항 정보를 DTO 형태로 조회하는 쿼리문
	 *
	 * <p> Dto Projection 기반으로 필요한 필드만 조회하며, 조회 성능이 중요할 때 사용됩니다.
	 *
	 * @param clubId   {설명: 클럽 고유 식별자}
	 * @param noticeId {설명: 공지사항 고유 식별자}
	 * @return Optional<InfoNoticeResponseDto> {설명: 공지사항 정보 DTO}
	 * @author sinyoung0403
	 */

	Optional<InfoNoticeResponseDto> findNoticeDtoById(Long clubId, Long noticeId);

	/**
	 * 설명: 공지사항 다건 조회하는 쿼리문
	 *
	 * <p> Dto Projection 적용, 페이징 처리
	 *
	 * @param clubId   {설명: 클럽 고유 식별자}
	 * @param pageable {설명: 페이징 객체}
	 * @return PageResponseDto<InfoNoticeResponseDto> {공지사항 정보 페이징 객체}
	 * @author sinyoung0403
	 */
	PageResponseDto<InfoNoticeResponseDto> findAllNotice(Long clubId, Pageable pageable);
}
