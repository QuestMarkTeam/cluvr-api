package com.example.cluvrapi.domain.replyChild.service;

import org.springframework.data.domain.Pageable;

import com.example.cluvrapi.domain.common.dto.PageResponseDto;
import com.example.cluvrapi.domain.replyChild.dto.request.CreateReplyChildRequestDto;
import com.example.cluvrapi.domain.replyChild.dto.request.UpdateReplyChildRequestDto;
import com.example.cluvrapi.domain.replyChild.dto.response.ReadReplyChildrenResponseDto;

public interface ReplyChildService {
	/**
	 * 설명: 특정 부모 댓글(reply)에 대한 자식 댓글(replyChild)을 생성합니다.
	 *
	 * <p>
	 * 사용자가 특정 댓글에 대댓글을 작성할 수 있으며, 멘션 유저가 포함될 수 있습니다.
	 *
	 * @param userId 댓글을 작성하는 사용자 ID
	 * @param boardId 부모 댓글 ID
	 * @param dto 생성 요청 데이터 (내용, 멘션 대상 ID 포함)
	 * @return 생성된 자식 댓글 ID
	 * @throws NoSuchElementException 해당 사용자나 부모 댓글, 멘션 대상이 존재하지 않을 경우
	 * @author yong
	 */
	long createReply(long userId, long boardId, CreateReplyChildRequestDto dto);

	/**
	 * 설명: 특정 부모 댓글에 달린 모든 자식 댓글(replyChild)을 페이징하여 조회합니다.
	 *
	 * <p>
	 * 댓글의 계층 구조를 표현하며, 무한 스크롤 또는 페이지네이션 처리에 사용됩니다.
	 *
	 * @param replyId 부모 댓글 ID
	 * @param pageable 페이징 정보 (페이지 번호, 크기 등)
	 * @return 자식 댓글 목록에 대한 페이지 응답 DTO
	 * @throws NoSuchElementException 해당 부모 댓글이 존재하지 않을 경우
	 * @author yong
	 */
	PageResponseDto<ReadReplyChildrenResponseDto> readReplyChildren(long replyId, Pageable pageable);

	/**
	 * 설명: 사용자가 본인의 자식 댓글(replyChild)을 수정합니다.
	 *
	 * <p>
	 * 멘션 대상이나 댓글 내용을 변경할 수 있으며, 본인만 수정 가능합니다.
	 *
	 * @param userId 요청 사용자 ID
	 * @param replyChildId 수정할 자식 댓글 ID
	 * @param dto 수정 요청 데이터 (멘션 대상 ID, 내용)
	 * @throws NoPermissionException 댓글 작성자가 아닌 사용자가 수정 시도한 경우
	 * @throws NoSuchElementException 해당 댓글이나 사용자 또는 멘션 대상이 존재하지 않을 경우
	 * @author yong
	 */
	void updateReplyChild(long userId, long replyChildId, UpdateReplyChildRequestDto dto);

	/**
	 * 설명: 사용자가 본인의 자식 댓글(replyChild)을 삭제합니다.
	 *
	 * <p>
	 * 댓글 작성자만 삭제할 수 있으며, 권한이 없을 경우 예외가 발생합니다.
	 *
	 * @param userId 요청 사용자 ID
	 * @param replyChildId 삭제할 자식 댓글 ID
	 * @throws NoPermissionException 댓글 작성자가 아닌 사용자가 삭제 시도한 경우
	 * @throws NoSuchElementException 해당 댓글이나 사용자가 존재하지 않을 경우
	 * @author yong
	 */
	void deleteReplyChild(long userId, long replyChildId);
}
