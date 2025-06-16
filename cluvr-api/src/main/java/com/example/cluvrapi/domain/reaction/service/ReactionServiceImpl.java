package com.example.cluvrapi.domain.reaction.service;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.cluvrapi.domain.board.entity.Board;
import com.example.cluvrapi.domain.board.repository.BoardRepository;
import com.example.cluvrapi.domain.notification.enums.NotiTargetType;
import com.example.cluvrapi.domain.notification.enums.NotificationType;
import com.example.cluvrapi.domain.notification.event.NotificationEvent;
import com.example.cluvrapi.domain.notification.event.NotificationProducer;
import com.example.cluvrapi.domain.reaction.dto.request.ReactionRequestDto;
import com.example.cluvrapi.domain.reaction.entity.Reaction;
import com.example.cluvrapi.domain.reaction.repository.ReactionRepository;
import com.example.cluvrapi.domain.reply.entity.Reply;
import com.example.cluvrapi.domain.reply.repository.ReplyRepository;
import com.example.cluvrapi.domain.user.entity.User;
import com.example.cluvrapi.domain.user.repository.UserRepository;
import com.example.cluvrapi.global.exception.BusinessException;
import com.example.cluvrapi.global.exception.SelfReactionNotAllowedException;
import com.example.cluvrapi.global.response.ResponseCode;

@Service
@RequiredArgsConstructor
public class ReactionServiceImpl implements ReactionService {
	private final ReactionRepository reactionRepository;
	private final BoardRepository boardRepository;
	private final ReplyRepository replyRepository;
	private final UserRepository userRepository;
	private final NotificationProducer notificationProducer;

	@Override
	@Transactional
	public void selectReaction(long userId, ReactionRequestDto dto) {
		User user = userRepository.findByIdOrElseThrow(userId);
		Board board = boardRepository.findByIdOrElseThrow(dto.getBoardId());

		Reply reply = null;
		// 댓글 리액션일 때
		if (dto.getReplyId() != null) {
			reply = replyRepository.findByIdOrElseThrow(dto.getReplyId());
			if (!reply.getBoard().getId().equals(board.getId())) {
				throw new BusinessException(ResponseCode.BOARD_REPLY_MISMATCH,
					ResponseCode.BOARD_REPLY_MISMATCH.getDefaultMessage());
			}
		}

		Reaction reaction = reactionRepository.findReaction(user, board, reply).orElse(null);
		// 리액션 존재 할 때
		if (reaction != null) {
			// 동일한 리액션 타입까지 가지고 있을 때
			if (reaction.getReactionType() == dto.getReactionType()) {
				throw new SelfReactionNotAllowedException(ResponseCode.SELF_REACTION_NOT_ALLOWED);
			}
			// 다른 리액션 타입을 가지고 있을 때 업데이트
			reaction.update(dto.getReactionType());
		} else {
			// 리액션이 존재하지 않을 때
			reactionRepository.save(new Reaction(user, board, reply, dto.getReactionType()));
		}

		// 알림
		if (board.getUser() != user && reply == null) {
			String content = String.format("'%s'님이 회원님의 게시글에 '%s'를 남겼습니다.", user.getName(),
				dto.getReactionType().name());

			NotificationEvent event = NotificationEvent.from(
				board.getUser().getId(),
				NotificationType.REACTION,
				content,
				NotiTargetType.BOARD,
				board.getId()
			);

			notificationProducer.send(event);
		}
	}

	@Override
	@Transactional
	public void cancelReaction(long userId, ReactionRequestDto dto) {
		User user = userRepository.findByIdOrElseThrow(userId);
		Board board = boardRepository.findByIdOrElseThrow(dto.getBoardId());
		Reply reply = null;
		if (dto.getReplyId() != null) {
			reply = replyRepository.findByIdOrElseThrow(dto.getReplyId());
			if (!reply.getBoard().getId().equals(board.getId())) {
				throw new BusinessException(ResponseCode.BOARD_REPLY_MISMATCH);
			}
		}

		Reaction reaction = reactionRepository.findReaction(user, board, reply).orElse(null);

		if (reaction == null) {
			throw new BusinessException(ResponseCode.NOT_FOUND, "저장된 기록이 없습니다.");
		}
		if (reaction.getReactionType() != dto.getReactionType()) {
			throw new BusinessException(ResponseCode.INVALID_REQUEST, "이 리액션 취소는 불가능합니다.");
		}

		reactionRepository.deleteById(reaction.getId());
	}
}
