package com.example.cluvrapi.domain.replyChild.service;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.cluvrapi.domain.reply.entity.Reply;
import com.example.cluvrapi.domain.reply.repository.ReplyRepository;
import com.example.cluvrapi.domain.replyChild.dto.CreateReplyChildRequestDto;
import com.example.cluvrapi.domain.replyChild.entity.MentionInfo;
import com.example.cluvrapi.domain.replyChild.entity.ReplyChild;
import com.example.cluvrapi.domain.replyChild.repository.ReplyChildRepository;
import com.example.cluvrapi.domain.user.entity.User;
import com.example.cluvrapi.domain.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class ReplyChildServiceImpl implements ReplyChildService {

	private final UserRepository userRepository;
	private final ReplyRepository replyRepository;
	private final ReplyChildRepository replyChildRepository;

	@Override
	@Transactional
	public long createReply(long userId, long replyId, CreateReplyChildRequestDto dto) {
		User user = userRepository.findByIdOrElseThrow(userId);
		Reply parentReply = replyRepository.findByIdOrElseThrow(replyId);
		User mention = userRepository.findByIdOrElseThrow(dto.getMentionId());
		MentionInfo mentionInfo = new MentionInfo(mention.getId(), mention.getName());

		ReplyChild replyChild = new ReplyChild(user, dto.getContent(), parentReply, mentionInfo);
		return replyChildRepository.save(replyChild).getId();
	}

}
