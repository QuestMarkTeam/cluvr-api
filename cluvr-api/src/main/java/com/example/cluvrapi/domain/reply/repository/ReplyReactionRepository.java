package com.example.cluvrapi.domain.reply.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.cluvrapi.domain.reply.entity.ReplyReactions;

public interface ReplyReactionRepository extends JpaRepository<ReplyReactions, Long>, ReplyReactionRepositoryCustom {

}
