package com.example.cluvrapi.domain.replyChild.repository;

import org.springframework.data.domain.Pageable;

import com.example.cluvrapi.domain.common.dto.PageResponseDto;
import com.example.cluvrapi.domain.replyChild.dto.response.ReadReplyChildrenResponseDto;

public interface ReplyChildRepositoryCustom {
	/**
	 * 설명: 특정 부모 댓글(reply)에 속한 모든 자식 댓글(replyChild)을 페이징하여 조회합니다.
	 *
	 * <p>
	 * 페이징 처리된 자식 댓글 데이터를 반환하며, 무한 스크롤 또는 페이지네이션 UI 구현 시 사용됩니다.
	 *
	 * @param replyId 부모 댓글 ID
	 * @param pageable 페이지 요청 정보 (페이지 번호, 크기, 정렬 등)
	 * @return 자식 댓글 목록이 포함된 페이지 응답 DTO
	 * @throws NoSuchElementException 해당 부모 댓글이 존재하지 않을 경우 (상황에 따라)
	 * @author yong
	 */
	PageResponseDto<ReadReplyChildrenResponseDto> findAllReplyChildrenByParent(long replyId, Pageable pageable);
}
