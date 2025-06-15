package com.example.cluvrapi.domain.notice.service;

import org.springframework.data.domain.Pageable;

import com.example.cluvrapi.domain.common.dto.PageResponseDto;
import com.example.cluvrapi.domain.notice.dto.reqeust.CreateNoticeRequestDto;
import com.example.cluvrapi.domain.notice.dto.reqeust.UpdateNoticeRequestDto;
import com.example.cluvrapi.domain.notice.dto.response.CreateNoticeResponseDto;
import com.example.cluvrapi.domain.notice.dto.response.InfoNoticeResponseDto;
import com.example.cluvrapi.global.exception.BusinessException;

public interface NoticeService {
	/**
	 * 설명: 공지사항을 생성하는 메서드
	 *
	 * @param userId                 {설명: 유저 고유 식별자}
	 * @param clubId                 {설명: 클럽 고유 식별자}
	 * @param createNoticeRequestDto {설명: Notice 생성 시 필요한 정보}
	 * @return CreateNoticeResponseDto {설명: Notice id 반환}
	 * @throws BusinessException {404 NotFound}
	 * @author {sinyoung0403}
	 */
	CreateNoticeResponseDto createNotice(Long userId, Long clubId, CreateNoticeRequestDto createNoticeRequestDto);

	/**
	 * 설명: 공지사항 단건 조회 메서드
	 *
	 * @param clubId   {설명: 클럽 고유 식별자}
	 * @param noticeId {설명: 공지사항 고유 식별자}
	 * @return InfoNoticeResponseDto {공지사항 정보}
	 * @author {sinyoung0403}
	 */
	InfoNoticeResponseDto findNoticeById(Long clubId, Long noticeId);

	/**
	 * 설명: 특정 클럽에 대한 모든 공지사항 조회
	 *
	 * @param clubId   {설명: 클럽 고유 식별자}
	 * @param pageable {설명: Pageable 객체}
	 * @return PageResponseDto<InfoNoticeResponseDto> {페이징 처리 된 공지사항 정보}
	 * @author {sinyoung0403}
	 */
	PageResponseDto<InfoNoticeResponseDto> findAllNotice(Long clubId, Pageable pageable);

	/**
	 * 설명: 공지사항 업데이트하는 메서드
	 *
	 * @param userId                 {설명: 유저 고유 식별자}
	 * @param clubId                 {설명: 클럽 고유 식별자}
	 * @param noticeId               {설명: 공지사항 고유 식별자}
	 * @param updateNoticeRequestDto {설명: 공지사항 업데이트할 정보}
	 * @throws BusinessException {404 NotFound}
	 * @author {sinyoung0403}
	 */
	void updateNotice(Long userId, Long clubId, Long noticeId, UpdateNoticeRequestDto updateNoticeRequestDto);

	/**
	 * 설명: 공지사항 삭제하는 메서드
	 *
	 * <p>{SoftDeleted 적용이 된 상태로 `isDeleted` 열이 true 가 된다.}
	 *
	 * @param userId   {설명: 유저 고유 식별자}
	 * @param clubId   {설명: 클럽 고유 식별자}
	 * @param noticeId {설명: 공지사항 고유 식별자}
	 * @throws BusinessException {404 NotFound}
	 * @author {sinyoung0403}
	 */
	void deleteNotice(Long userId, Long clubId, Long noticeId);
}
