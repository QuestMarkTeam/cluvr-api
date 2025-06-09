package com.example.cluvrapi.domain.board.repository;

import com.example.cluvrapi.domain.board.entity.Board;
import com.example.cluvrapi.domain.user.entity.User;

public interface BoardReactionRepositoryCustom {
	void deleteByUserAndBoard(User user, Board board);
}
