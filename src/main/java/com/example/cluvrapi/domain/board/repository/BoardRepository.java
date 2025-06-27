package com.example.cluvrapi.domain.board.repository;

import io.lettuce.core.dynamic.annotation.Param;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.cluvrapi.domain.board.entity.Board;
import com.example.cluvrapi.domain.common.repository.BaseRepository;

public interface BoardRepository extends BaseRepository<Board, Long>, BoardRepositoryCustom {
	@Modifying
	@Query("UPDATE Board b SET b.viewCount = b.viewCount + 1 WHERE b.id = :id")
	void incrementViewCount(@Param("id") long id);

	List<Board> findByIdIn(List<Long> ids);
}
