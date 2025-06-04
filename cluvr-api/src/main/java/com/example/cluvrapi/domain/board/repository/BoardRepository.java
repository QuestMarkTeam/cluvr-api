package com.example.cluvrapi.domain.board.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.cluvrapi.domain.board.entity.Board;
import com.example.cluvrapi.domain.category.enums.CategoryType;
import com.example.cluvrapi.domain.common.repository.BaseRepository;

public interface BoardRepository extends BaseRepository<Board, Long> {
	Page<Board> findAllByCategory(Pageable pageable, CategoryType category);
}
