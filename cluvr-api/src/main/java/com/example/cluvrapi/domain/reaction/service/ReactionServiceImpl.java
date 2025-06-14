package com.example.cluvrapi.domain.reaction.service;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.cluvrapi.domain.board.entity.Board;
import com.example.cluvrapi.domain.board.repository.BoardRepository;
import com.example.cluvrapi.domain.reaction.dto.ReactionRequestDto;
import com.example.cluvrapi.domain.reaction.entity.Reaction;
import com.example.cluvrapi.domain.reaction.repository.ReactionRepository;
import com.example.cluvrapi.domain.reply.entity.Reply;
import com.example.cluvrapi.domain.reply.repository.ReplyRepository;
import com.example.cluvrapi.domain.user.entity.User;
import com.example.cluvrapi.domain.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class ReactionServiceImpl implements ReactionService {
	private final ReactionRepository reactionRepository;
	private final BoardRepository boardRepository;
	private final ReplyRepository replyRepository;
	private final UserRepository userRepository;

	@Override
	@Transactional
	public void selectReaction(long userId, ReactionRequestDto dto) {
		User user = userRepository.findByIdOrElseThrow(userId);
		Board board = boardRepository.findByIdOrElseThrow(dto.getBoardId());
		Reply reply = null;
		if (dto.getReplyId() != null) {
			reply = replyRepository.findByIdOrElseThrow(dto.getReplyId());
		}

		Reaction reaction = reactionRepository.findReaction(user, board, reply).orElse(null);

		if (reaction != null) {
			if (reaction.getReactionType() == dto.getReactionType()) {
				// throw new SelfReactionNotAllowedException(ResponseCode.SELF_REACTION_NOT_ALLOWED);
				throw new RuntimeException("실패1");
			}

			reaction.update(dto.getReactionType());
		} else {
			long reactionId = reactionRepository.save(new Reaction(user, board, reply, dto.getReactionType())).getId();
			System.out.println(reactionId);
		}
	}

	@Override
	@Transactional
	public void cancelReaction(long userId, ReactionRequestDto dto) {

	}
}
