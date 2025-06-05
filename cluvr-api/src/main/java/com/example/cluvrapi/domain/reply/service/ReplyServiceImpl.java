package com.example.cluvrapi.domain.reply.service;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.cluvrapi.domain.board.entity.Board;
import com.example.cluvrapi.domain.board.repository.BoardRepository;
import com.example.cluvrapi.domain.reply.dto.request.CreateReplyRequestDto;
import com.example.cluvrapi.domain.reply.dto.response.ReadReplyResponseDto;
import com.example.cluvrapi.domain.reply.entity.Reply;
import com.example.cluvrapi.domain.reply.repository.ReplyRepository;
import com.example.cluvrapi.domain.user.entity.User;
import com.example.cluvrapi.domain.user.repository.UserRepository;

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
	public List<ReadReplyResponseDto> readReplies(long boardId, Long parentId, int pageNumber,
		int pageSize) {
		return replyRepository.findAllRepliesByParent(boardId, parentId, pageNumber, pageSize);
	}
}
