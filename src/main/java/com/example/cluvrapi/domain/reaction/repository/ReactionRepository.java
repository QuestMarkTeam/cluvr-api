package com.example.cluvrapi.domain.reaction.repository;

import com.example.cluvrapi.domain.common.repository.BaseRepository;
import com.example.cluvrapi.domain.reaction.entity.Reaction;

public interface ReactionRepository extends BaseRepository<Reaction, Long>, ReactionRepositoryCustom {
	
}
