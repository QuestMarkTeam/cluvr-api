package com.example.cluvrapi.domain.board.service;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.cluvrapi.domain.board.dto.request.CreateBoardRequestDto;
import com.example.cluvrapi.domain.board.dto.request.UpdateBoardRequestDto;
import com.example.cluvrapi.domain.board.dto.response.ReadBoardResponseDto;
import com.example.cluvrapi.domain.board.dto.response.ReadBoardsResponseDto;
import com.example.cluvrapi.domain.board.entity.Board;
import com.example.cluvrapi.domain.board.repository.BoardRepository;
import com.example.cluvrapi.domain.category.enums.CategoryType;
import com.example.cluvrapi.domain.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

	private final UserRepository userRepository;
	private final BoardRepository boardRepository;

	@Override
	@Transactional
	public long createBoard(CreateBoardRequestDto dto) {
		return boardRepository.save(dto.fromDto()).getId();
	}

	@Override
	@Transactional(readOnly = true)
	public Page<ReadBoardsResponseDto> readBoards(CategoryType category, int pageNumber, int pageSize) {
		Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);

		Page<Board> boards = boardRepository.findAllByCategory(pageable, category);

		return boards.map(board -> new ReadBoardsResponseDto(
			board.getId(),
			board.getTitle(),
			board.getContent(),
			board.getView(),
			board.getCreatedAt(),
			board.getModifiedAt()
		));
	}

	@Override
	@Transactional(readOnly = true)
	public ReadBoardResponseDto readBoard(long boardId) {
		Board board = boardRepository.findByIdOrElseThrow(boardId);
		return new ReadBoardResponseDto(
			board.getId(),
			board.getTitle(),
			board.getContent(),
			board.getCategory(),
			board.isSelected(),
			board.getClover(),
			board.getView(),
			board.getCreatedAt(),
			board.getModifiedAt()
		);
	}

	@Transactional
	@Override
	public void updateBoard(UpdateBoardRequestDto dto, long boardId) {
		Board board = boardRepository.findByIdOrElseThrow(boardId);
		board.update(dto.getTitle(), dto.getContent(), dto.getClover());
	}
}
