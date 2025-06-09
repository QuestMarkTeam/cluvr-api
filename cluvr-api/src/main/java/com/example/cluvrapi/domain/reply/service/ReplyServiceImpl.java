package com.example.cluvrapi.domain.reply.service;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.cluvrapi.domain.board.entity.Board;
import com.example.cluvrapi.domain.board.repository.BoardRepository;
import com.example.cluvrapi.domain.common.dto.PageResponseDto;
import com.example.cluvrapi.domain.reply.dto.request.CreateReplyRequestDto;
import com.example.cluvrapi.domain.reply.dto.request.UpdateReplyRequestDto;
import com.example.cluvrapi.domain.reply.dto.response.ReadReplyResponseDto;
import com.example.cluvrapi.domain.reply.entity.Reply;
import com.example.cluvrapi.domain.reply.repository.ReplyRepository;
import com.example.cluvrapi.domain.user.entity.User;
import com.example.cluvrapi.domain.user.repository.UserRepository;
import com.example.cluvrapi.global.exception.AuthenticationException;
import com.example.cluvrapi.global.response.ResponseCode;

@Service
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService {
	private final ReplyRepository replyRepository;
	private final UserRepository userRepository;
	private final BoardRepository boardRepository;

	@Transactional
	@Override
	public Long createReply(long userId, long boardId, CreateReplyRequestDto dto) {
		User user = userRepository.findByIdOrElseThrow(userId);
		Board board = boardRepository.findByIdOrElseThrow(boardId);
		Reply parent = null;
		if (dto.getParentId() != null) {
			parent = replyRepository.findByIdOrElseThrow(dto.getParentId());
		}

		Reply reply = dto.fromDto(user, board, parent);
		return replyRepository.save(reply).getId();
	}

	@Transactional(readOnly = true)
	@Override
	public PageResponseDto<ReadReplyResponseDto> readReplies(long boardId, Long parentId, Pageable pageable) {
		return replyRepository.findAllRepliesByParent(boardId, parentId, pageable);
	}

	@Transactional
	@Override
	public void updateReply(long userId, long boardId, long replyId, UpdateReplyRequestDto dto) {
		Reply reply = replyRepository.findByIdOrElseThrow(replyId);
		isValid(userId, reply.getUser());
		reply.update(dto.getContent());
	}

	@Override
	public void deleteReply(long userId, long boardId, long replyId) {
		Reply reply = replyRepository.findByIdOrElseThrow(replyId);
		isValid(userId, reply.getUser());
		reply.delete();
	}

	private void isValid(long userId, User replyUser) throws AuthenticationException {
		if (userId != replyUser.getId()) {
			throw new AuthenticationException(ResponseCode.AUTH_ANNOTATION_USER_MISMATCH);
		}
	}
}
