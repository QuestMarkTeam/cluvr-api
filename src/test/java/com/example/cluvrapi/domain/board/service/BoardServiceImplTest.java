package com.example.cluvrapi.domain.board.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import com.example.cluvrapi.domain.board.dto.request.CreateBoardRequestDto;
import com.example.cluvrapi.domain.board.dto.request.UpdateBoardRequestDto;
import com.example.cluvrapi.domain.board.dto.response.ReadBoardResponseDto;
import com.example.cluvrapi.domain.board.dto.response.ReadBoardsResponseDto;
import com.example.cluvrapi.domain.board.dto.response.ReadMyBoardsResponseDto;
import com.example.cluvrapi.domain.board.entity.Board;
import com.example.cluvrapi.domain.board.enums.BoardType;
import com.example.cluvrapi.domain.board.repository.BoardRepository;
import com.example.cluvrapi.domain.category.enums.CategoryType;
import com.example.cluvrapi.domain.clover.entity.Clover;
import com.example.cluvrapi.domain.clover.repository.CloverRepository;
import com.example.cluvrapi.domain.common.dto.PageResponseDto;
import com.example.cluvrapi.domain.reaction.enums.ReactionType;
import com.example.cluvrapi.domain.reaction.repository.ReactionRepository;
import com.example.cluvrapi.domain.reply.entity.Reply;
import com.example.cluvrapi.domain.reply.repository.ReplyRepository;
import com.example.cluvrapi.domain.user.entity.User;
import com.example.cluvrapi.domain.user.entity.enums.Gender;
import com.example.cluvrapi.domain.user.entity.enums.UserRole;
import com.example.cluvrapi.domain.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class BoardServiceImplTest {

    @InjectMocks
    BoardServiceImpl boardService;

    @Mock
    BoardRepository boardRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    ReactionRepository reactionRepository;

    @Mock
    ReplyRepository replyRepository;

    @Mock
    CloverRepository cloverRepository;


	@Test
	@DisplayName("게시글 생성")
	void createBoard() {
		//given
		Long userId = 123L;
		User user = new User(userId, "minsu", LocalDate.parse("2000-12-25"),"test@email.com","010-1234-5678", UserRole.USER, Gender.MALE, CategoryType.DEVELOPMENT, "1234", 100, "imageUrl", false);
		CreateBoardRequestDto dto = new CreateBoardRequestDto(
			"title",
			"content",
			BoardType.CHITCHAT,
			CategoryType.DEVELOPMENT,
			20
		);
		Board board = new Board(user, BoardType.CHITCHAT, CategoryType.DEVELOPMENT, "title", "content", 20);
		ReflectionTestUtils.setField(board, "id", 1L);

		given(boardRepository.save(any(Board.class))).willReturn(board); // save()는 내부에서 dto.fromDto(user) 같은 식으로 새 객체를 만들기 때문에, 어떤 Board 객체가 올지 테스트 입장에서 정확히 예측 불가.
		given(userRepository.findByIdOrElseThrow(userId)).willReturn(user); // 반면, userRepository.findByIdOrElseThrow(userId)는 파라미터가 Long이고, 네가 아는 값(userId)이 들어가니까 매칭을 정확히 할 수 있음.

		//when
		Long boardId = boardService.createBoard(userId, dto);
		// 서비스 안에서 boardRepository.save(dto.fromDto(user)) 호출

		//then
		assertEquals(1l, boardId);
		then(userRepository).should().findByIdOrElseThrow(userId); // should : BDD 스타일 호출 검증 방식

		// 결론은 dto.fromDto 때문에 값 추적이 안되니까 ArgumentCapture로 dto로 전달된걸로 잘 생성됐는지 비교
		ArgumentCaptor<Board> captor = ArgumentCaptor.forClass(Board.class);
		then(boardRepository).should().save(captor.capture());
		Board savedBoard = captor.getValue(); // 실제 서비스 내부에서 save()에 전달된 객체
		assertEquals("title", savedBoard.getTitle());
		assertEquals("content", savedBoard.getContent());
		assertEquals(user, savedBoard.getUser());
	}

	@Test
	@DisplayName("게시판 읽어오기")
	void readBoards() {
		//given
		Pageable pageable = PageRequest.of(0, 5);
		List<ReadBoardsResponseDto> content = List.of(
			new ReadBoardsResponseDto(1L, "title1", "content1", 5, "nickname1", LocalDateTime.now(), LocalDateTime.now()),
			new ReadBoardsResponseDto(2L, "title2", "content2", 50, "nickname2", LocalDateTime.now(), LocalDateTime.now())
		);
		PageResponseDto<ReadBoardsResponseDto> fakeResponse =
			PageResponseDto.<ReadBoardsResponseDto>builder()
				.content(content)
				.page(1)
				.size(5)
				.totalElements(2L)
				.totalPages(1)
				.build();

		given(boardRepository.findAllBoardsByCategory(CategoryType.DEVELOPMENT,pageable)).willReturn(fakeResponse);

		//when
		PageResponseDto<ReadBoardsResponseDto> result = boardService.readBoards(CategoryType.DEVELOPMENT, pageable);

		//then
		assertEquals(2, result.getContent().size());
		assertEquals("title1", result.getContent().get(0).getTitle());
		assertEquals("title2", result.getContent().get(1).getTitle());
		then(boardRepository).should().findAllBoardsByCategory(CategoryType.DEVELOPMENT, pageable);
	}

	@Test
	@DisplayName("게시글 단건 읽어오기")
	void readBoard() {
		//given
		Long boardId = 1L;
		User user = new User(1L, "minsu", LocalDate.parse("2000-12-25"), "test@email.com", "010-1234-5678", UserRole.USER, Gender.MALE, CategoryType.DEVELOPMENT, "1234", 100, "imageUrl", false);
		Board board = new Board(user, BoardType.CHITCHAT, CategoryType.DEVELOPMENT, "title", "content", 10);
		ReflectionTestUtils.setField(board, "id", boardId);

		Map<ReactionType, Long> reactionCounts = new HashMap<>();
		reactionCounts.put(ReactionType.LIKE, 10L);
		reactionCounts.put(ReactionType.DISLIKE, 2L);

		doNothing().when(boardRepository).incrementViewCount(boardId);
		given(boardRepository.findBoardById(boardId)).willReturn(board);
		// given(reactionRepository.countBoardReactions(board)).willReturn(reactionCounts);

		//when
		// ReadBoardResponseDto response = boardService.readBoard(boardId);

		//then
		// assertNotNull(response);
		// assertEquals(board.getTitle(), response.getTitle());
		// assertEquals(board.getContent(), response.getContent());
		// assertEquals(10L, response.getLike());
		// assertEquals(2L, response.getDislike());
		// assertEquals(board.getCategory(), response.getCategory());
		// assertEquals(board.isSelected(), response.isSelected());
		// then(boardRepository).should().incrementViewCount(boardId);
		// then(boardRepository).should().findBoardById(boardId);
		// then(reactionRepository).should().countBoardReactions(board);
	}

	@Test
	@DisplayName("게시글 수정")
	void updateBoard() {
		//given
		Long userId = 1L;
		Long boardId = 10L;
		UpdateBoardRequestDto dto = new UpdateBoardRequestDto("newTitle","newContent",50);

		User user = new User(userId, "minsu", LocalDate.parse("2000-12-25"), "test@email.com", "010-1234-5678", UserRole.USER, Gender.MALE, CategoryType.DEVELOPMENT, "1234", 100, "imageUrl", false);
		Board board = new Board(user, BoardType.CHITCHAT, CategoryType.DEVELOPMENT, "oldTitle", "oldContent", 20);
		ReflectionTestUtils.setField(board, "id", boardId);

		given(userRepository.findByIdOrElseThrow(userId)).willReturn(user);
		given(boardRepository.findByIdOrElseThrow(boardId)).willReturn(board);

		//when
		boardService.updateBoard(userId, dto, boardId);

		//then
		assertEquals("newTitle", board.getTitle());
		assertEquals("newContent", board.getContent());
		assertEquals(70, board.getClover());
		then(userRepository).should().findByIdOrElseThrow(userId);
		then(boardRepository).should().findByIdOrElseThrow(boardId);
	}

	@Test
	@DisplayName("게시글 삭제")
	void deleteBoard() {
		//given
		Long userId = 1L;
		Long boardId = 10L;

		User user = new User(userId, "minsu", LocalDate.parse("2000-12-25"), "test@email.com", "010-1234-5678", UserRole.USER, Gender.MALE, CategoryType.DEVELOPMENT, "1234", 100, "imageUrl", false);
		Board board = new Board(user, BoardType.CHITCHAT, CategoryType.DEVELOPMENT, "title", "content", 20);
		ReflectionTestUtils.setField(board, "id", boardId);

		given(userRepository.findByIdOrElseThrow(userId)).willReturn(user);
		given(boardRepository.findByIdOrElseThrow(boardId)).willReturn(board);
		willDoNothing().given(boardRepository).delete(board);

		//when
		boardService.deleteBoard(userId, boardId);

		//then
		then(userRepository).should().findByIdOrElseThrow(userId);
		then(boardRepository).should().findByIdOrElseThrow(boardId);
		then(boardRepository).should().delete(board);
	}

	@Test
	@DisplayName("내가 쓴 게시글 목록 조회")
	void readBoardsWithUser() {
		//given
		Long userId = 1L;
		Pageable pageable = PageRequest.of(0, 5);
		List<ReadMyBoardsResponseDto> content = List.of(
			new ReadMyBoardsResponseDto(1L, "title1", "nickname1", LocalDateTime.now()),
			new ReadMyBoardsResponseDto(2L, "title2", "nickname2", LocalDateTime.now())
		);
		PageResponseDto<ReadMyBoardsResponseDto> fakeResponse =
			PageResponseDto.<ReadMyBoardsResponseDto>builder()
				.content(content)
				.page(1)
				.size(5)
				.totalElements(2L)
				.totalPages(1)
				.build();

		given(boardRepository.findBoardsByUser(userId, pageable)).willReturn(fakeResponse);

		//when
		PageResponseDto<ReadMyBoardsResponseDto> result = boardService.readBoardsWithUser(userId, pageable);

		//then
		assertEquals(2, result.getContent().size());
		assertEquals("title1", result.getContent().get(0).getTitle());
		then(boardRepository).should().findBoardsByUser(userId, pageable);
	}

	@Test
	@DisplayName("댓글 채택")
	void selectBestReply() {
		//given
		Long userId = 1L;
		Long boardId = 2L;
		Long replyId = 3L;

		User writer = new User(userId, "writer", LocalDate.parse("2000-12-25"), "test1@email.com", "010-1234-5678", UserRole.USER, Gender.MALE, CategoryType.DEVELOPMENT, "1234", 100, "imageUrl", false);
		User replier = new User(userId, "replier", LocalDate.parse("2000-12-25"), "test2@email.com", "010-1234-5678", UserRole.USER, Gender.MALE, CategoryType.DEVELOPMENT, "1234", 100, "imageUrl", false);

		Board board = new Board(writer, BoardType.CHITCHAT, CategoryType.DEVELOPMENT, "title", "content", 20);
		ReflectionTestUtils.setField(board, "id", boardId);

		Reply reply = new Reply(replier, "replyContent", board);
		ReflectionTestUtils.setField(reply, "id", replyId);

		Clover clover = mock(Clover.class);
		given(userRepository.findByIdOrElseThrow(userId)).willReturn(writer);
		given(boardRepository.findByIdOrElseThrow(boardId)).willReturn(board);
		given(replyRepository.findByIdOrElseThrow(replyId)).willReturn(reply);
		given(cloverRepository.findByUserIdOrElseThrow(replier.getId())).willReturn(clover);
		doNothing().when(clover).takeScore(board.getClover());

		//when
		boardService.selectBestReply(userId, boardId, replyId);

		//then
		assertTrue(board.isSelected());
		assertTrue(reply.isSelected());
		then(userRepository).should().findByIdOrElseThrow(userId);
		then(boardRepository).should().findByIdOrElseThrow(boardId);
		then(replyRepository).should().findByIdOrElseThrow(replyId);
		then(cloverRepository).should().findByUserIdOrElseThrow(replier.getId());
		then(clover).should().takeScore(board.getClover());
	}
}
