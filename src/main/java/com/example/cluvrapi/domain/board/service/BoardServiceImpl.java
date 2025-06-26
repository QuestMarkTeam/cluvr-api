package com.example.cluvrapi.domain.board.service;

import java.util.Map;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.cluvrapi.domain.board.dto.request.CreateBoardRequestDto;
import com.example.cluvrapi.domain.board.dto.request.UpdateBoardRequestDto;
import com.example.cluvrapi.domain.board.dto.response.ReadAllBoardsResponseDto;
import com.example.cluvrapi.domain.board.dto.response.ReadBoardResponseDto;
import com.example.cluvrapi.domain.board.dto.response.ReadMyBoardsResponseDto;
import com.example.cluvrapi.domain.board.entity.Board;
import com.example.cluvrapi.domain.board.repository.BoardRepository;
import com.example.cluvrapi.domain.category.enums.CategoryType;
import com.example.cluvrapi.domain.clover.entity.Clover;
import com.example.cluvrapi.domain.clover.enums.CloverUserActivityType;
import com.example.cluvrapi.domain.clover.repository.CloverRepository;
import com.example.cluvrapi.domain.common.dto.PageResponseDto;
import com.example.cluvrapi.domain.gem.enums.GemUserActivityType;
import com.example.cluvrapi.domain.notification.event.NotificationProducer;
import com.example.cluvrapi.domain.reaction.enums.ReactionType;
import com.example.cluvrapi.domain.reaction.repository.ReactionRepository;
import com.example.cluvrapi.domain.reply.entity.Reply;
import com.example.cluvrapi.domain.reply.repository.ReplyRepository;
import com.example.cluvrapi.domain.user.entity.User;
import com.example.cluvrapi.domain.user.repository.UserRepository;
import com.example.cluvrapi.global.exception.BusinessException;
import com.example.cluvrapi.global.annotation.EventGem;
import com.example.cluvrapi.global.annotation.UpdateClover;
import com.example.cluvrapi.global.exception.NoPermissionException;
import com.example.cluvrapi.global.response.ResponseCode;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

	private final UserRepository userRepository;
	private final BoardRepository boardRepository;
	private final NotificationProducer notificationProducer;
	private final ReactionRepository reactionRepository;
	private final ReplyRepository replyRepository;
	private final CloverRepository cloverRepository;
	private final BoardViewCountRedisService boardViewCountRedisService;

	@EventGem(value = GemUserActivityType.BOARD)
	@UpdateClover(value = CloverUserActivityType.CREATE_QUESTION)
	@Override
	@Transactional
	public long createBoard(long userId, CreateBoardRequestDto dto) {
		User user = userRepository.findByIdOrElseThrow(userId);
		return boardRepository.save(dto.fromDto(user)).getId();
	}

	@Override
	@Transactional(readOnly = true)
	public PageResponseDto<ReadAllBoardsResponseDto> readBoards(CategoryType category, Pageable pageable) {
		return boardRepository.findAllBoardsByCategory(category, pageable);
	}

	@Override
	@Transactional
	public ReadBoardResponseDto readBoard(long boardId, long userId) {
		//고도화 예정
		Board board = boardRepository.findBoardById(boardId);
		Map<ReactionType, Long> reactionCountMap = reactionRepository.countBoardReactions(board);

		// 좋아요 싫어요 수도 고도화 필요
		long likeCount = reactionCountMap.getOrDefault(ReactionType.LIKE, 0L);
		long dislikeCount = reactionCountMap.getOrDefault(ReactionType.DISLIKE, 0L);

		long viewCount = boardViewCountRedisService.incrementViewCount(board, userId);

		return ReadBoardResponseDto.ofDto(board, viewCount, likeCount, dislikeCount);
	}

	@Transactional
	@Override
	public void updateBoard(long userId, UpdateBoardRequestDto dto, long boardId) {
		User user = userRepository.findByIdOrElseThrow(userId);
		Board board = boardRepository.findByIdOrElseThrow(boardId);
		int clover = board.getClover();

		if (dto.getClover() != null) {
			clover += dto.getClover();
		}

		// 게시글을 작성한 유저인지 확인
		if (!board.getUser().equals(user)) {
			throw new NoPermissionException(ResponseCode.NO_PERMISSION_DELETE,
				ResponseCode.NO_PERMISSION_DELETE.getDefaultMessage());
		}

		// 클로버 합산 조건 확인 (dto.getClover()가 null일 수도 있음)
		if (dto.getClover() != null && clover > 100) {
			throw new BusinessException(ResponseCode.CLOVER_POST_LIMIT);
		}

		boardRepository.updateBoard(boardId, dto.getTitle(), dto.getContent(), clover);
		System.out.println("test");
	}

	@Transactional
	@Override
	public void deleteBoard(long userId, long boardId) {
		User user = userRepository.findByIdOrElseThrow(userId);
		Board board = boardRepository.findByIdOrElseThrow(boardId);

		if (!user.equals(board.getUser())) {
			throw new NoPermissionException(ResponseCode.NO_PERMISSION_DELETE,
				ResponseCode.NO_PERMISSION_DELETE.getDefaultMessage());
		}
		boardRepository.delete(board);
	}

	@Override
	@Transactional(readOnly = true)
	public PageResponseDto<ReadMyBoardsResponseDto> readBoardsWithUser(long userId, Pageable pageable) {
		return boardRepository.findBoardsByUser(userId, pageable);
	}

	@Override
	@Transactional
	public void selectBestReply(long userId, long boardId, long replyId) {
		User user = userRepository.findByIdOrElseThrow(userId);
		Board board = boardRepository.findByIdOrElseThrow(boardId);
		Reply reply = replyRepository.findByIdOrElseThrow(replyId);
		Clover clover = cloverRepository.findByUserIdOrElseThrow(reply.getUser().getId());

		// 보드의 isSelected가 true일 경우 사용 불가능
		if (board.isSelected()) {
			throw new BusinessException(ResponseCode.ALREADY_SELECTED);

		}
		// 게시글을 작성한 유저가 아닐 경우 채택 불가능
		if (!user.equals(board.getUser())) {
			throw new NoPermissionException(ResponseCode.ACCESS_DENIED, ResponseCode.ACCESS_DENIED.getDefaultMessage());
		}

		// 게시글 유저와 댓글 유저가 동일하면 채택 불가능
		if (board.getUser().equals(reply.getUser())) {
			throw new BusinessException(ResponseCode.CANNOT_SELECT_OWN_REPLY);
		}

		// 댓글이 게시글 소속의 댓글이 아니면 채택 불가능
		if (!board.equals(reply.getBoard())) {
			throw new BusinessException(ResponseCode.REPLY_NOT_MATCHED_WITH_BOARD);
		}

		// 게시글에 있던 클로버를 댓글의 주인에게 클로버를 지급한다.
		clover.takeScore(board.getClover());

		// 보드 엔티티에서 is_selected가 true가 된다.
		board.updateSelection();

		// 댓글 엔티티에서 is_selected가 true가 된다.
		reply.updateSelection();

		// 알림
		// 은세님 알림 이런 식으로 만들면 되는지 확인해주세요.
		// if (board.getUser() != user) {
		// 	String content = String.format("'%s'님이 '%s' 게시글에서 회원님의 댓글을 채택하셨습니다. /n 보상으로 '%s' clover를 지급해드립니다.",
		// 		user.getName(),
		// 		board.getTitle(), board.getGem());
		//
		// 	NotificationEvent event = NotificationEvent.from(
		// 		board.getUser().getId(),
		// 		NotificationType.REACTION,
		// 		content,
		// 		NotiTargetType.BOARD,
		// 		board.getId()
		// 	);
		//
		// 	notificationProducer.send(event);
		// }
	}
}
