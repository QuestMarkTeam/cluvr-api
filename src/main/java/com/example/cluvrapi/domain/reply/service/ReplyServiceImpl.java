package com.example.cluvrapi.domain.reply.service;

import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.cluvrapi.domain.board.entity.Board;
import com.example.cluvrapi.domain.board.repository.BoardRepository;
import com.example.cluvrapi.domain.category.enums.CategoryType;
import com.example.cluvrapi.domain.common.dto.PageResponseDto;
import com.example.cluvrapi.domain.notification.event.NotificationProducer;
import com.example.cluvrapi.domain.reply.dto.request.CreateReplyRequestDto;
import com.example.cluvrapi.domain.reply.dto.request.UpdateReplyRequestDto;
import com.example.cluvrapi.domain.reply.dto.response.ReadMyReplyResponseDto;
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
	private final NotificationProducer notificationProducer;
	private final ReplyReactionRedisService replyReactionCountRedisService;

	@Transactional
	@Override
	public Long createReply(long userId, long boardId, CreateReplyRequestDto dto) {
		User user = userRepository.findByIdOrElseThrow(userId);
		Board board = boardRepository.findByIdOrElseThrow(boardId);

		Reply reply = dto.fromDto(user, board);
		Long savedId = replyRepository.save(reply).getId();

		// 알림
		// if (dto.getParentId() == null && !user.getId().equals(board.getUser().getId())) {
		// 	Long receiverId = board.getUser().getId();
		// 	String content = String.format("'%s'님이 회원님의 게시글에 댓글을 남겼습니다.", user.getName());
		//
		// 	NotificationEvent event = NotificationEvent.from(
		// 		receiverId,
		// 		NotificationType.REPLY,
		// 		content,
		// 		NotiTargetType.BOARD,
		// 		boardId
		// 	);
		//
		// 	notificationProducer.send(event);
		// }

		return savedId;
	}

	@Transactional(readOnly = true)
	@Override
	public PageResponseDto<ReadReplyResponseDto> readReplies(long boardId, Pageable pageable) {
		PageResponseDto<Reply> replies = replyRepository.findAllRepliesByBoard(boardId, pageable);

		List<ReadReplyResponseDto> dtos = replies.getContent().stream()
			.map(reply -> {
				long like = replyReactionCountRedisService.readLikeCountFromRedis(reply.getId());
				long dislike = replyReactionCountRedisService.readDislikeCountFromRedis(reply.getId());
				return new ReadReplyResponseDto(reply, like, dislike);
			}).toList();

		return PageResponseDto.toDto(new PageImpl<>(dtos, pageable, replies.getTotalElements()));
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
		replyRepository.delete(reply);
	}

	public PageResponseDto<ReadMyReplyResponseDto> readRepliesWithUser(long userId, Pageable pageable) {
		return replyRepository.findRepliesByUser(userId, pageable);
	}

	@Override
	public Map<CategoryType, Long> readReplyCountPerCategoryByUser(long userId) {
		return replyRepository.countRepliesPerCategoryByUser(userId);
	}

	private void isValid(long userId, User replyUser) throws AuthenticationException {
		if (userId != replyUser.getId()) {
			throw new AuthenticationException(ResponseCode.AUTH_ANNOTATION_USER_MISMATCH);
		}
	}
}
