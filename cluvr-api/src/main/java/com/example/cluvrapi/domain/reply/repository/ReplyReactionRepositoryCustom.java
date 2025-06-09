package com.example.cluvrapi.domain.reply.repository;

import com.example.cluvrapi.domain.reply.entity.Reply;
import com.example.cluvrapi.domain.user.entity.User;

public interface ReplyReactionRepositoryCustom {
	void deleteByUserAndReply(User user, Reply reply);
}
