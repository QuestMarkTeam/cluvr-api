package com.example.cluvrapi.domain.board.service;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.cluvrapi.domain.board.dto.request.CreateBoardRequestDto;
import com.example.cluvrapi.domain.board.dto.request.UpdateBoardRequestDto;
import com.example.cluvrapi.domain.board.dto.response.ReadBoardResponseDto;
import com.example.cluvrapi.domain.board.dto.response.ReadBoardsResponseDto;
import com.example.cluvrapi.domain.board.dto.response.ReadMyBoardsResponseDto;
import com.example.cluvrapi.domain.board.entity.Board;
import com.example.cluvrapi.domain.board.repository.BoardRepository;
import com.example.cluvrapi.domain.category.enums.CategoryType;
import com.example.cluvrapi.domain.common.dto.PageResponseDto;
import com.example.cluvrapi.domain.notification.event.NotificationProducer;
import com.example.cluvrapi.domain.user.entity.User;
import com.example.cluvrapi.domain.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

	private final UserRepository userRepository;
	private final BoardRepository boardRepository;
	private final NotificationProducer notificationProducer;

	@Override
	@Transactional
	public long createBoard(CreateBoardRequestDto dto) {
		User user = userRepository.findByIdOrElseThrow(1L);
		return boardRepository.save(dto.fromDto(user)).getId();
	}

	@Override
	@Transactional(readOnly = true)
	public List<ReadBoardsResponseDto> readBoards(CategoryType category, int pageNumber, int pageSize) {
		return boardRepository.findAllBoardsByCategory(category, pageNumber, pageSize);
	}

	@Override
	@Transactional(readOnly = true)
	public ReadBoardResponseDto readBoard(long boardId) {
		Board board = boardRepository.findBoardById(boardId);
		return ReadBoardResponseDto.ofDto(board);
	}

	@Transactional
	@Override
	public void updateBoard(long userId, UpdateBoardRequestDto dto, long boardId) {
		User user = userRepository.findByIdOrElseThrow(userId);
		Board board = boardRepository.findByIdOrElseThrow(boardId);
		board.update(dto.getTitle(), dto.getContent(), dto.getClover());
	}

	@Transactional
	@Override
	public void deleteBoard(long boardId) {
		Board board = boardRepository.findByIdOrElseThrow(boardId);
		board.delete();
	}

	@Override
	@Transactional(readOnly = true)
	public PageResponseDto<ReadMyBoardsResponseDto> readBoardsWithUser(long userId, Pageable pageable) {
		return boardRepository.findBoardsByUser(userId, pageable);
	}
}
