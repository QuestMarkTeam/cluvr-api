package com.example.cluvrapi.domain.replyChild.service;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.cluvrapi.domain.common.dto.PageResponseDto;
import com.example.cluvrapi.domain.reply.entity.Reply;
import com.example.cluvrapi.domain.reply.repository.ReplyRepository;
import com.example.cluvrapi.domain.replyChild.dto.request.CreateReplyChildRequestDto;
import com.example.cluvrapi.domain.replyChild.dto.request.UpdateReplyChildRequestDto;
import com.example.cluvrapi.domain.replyChild.dto.response.ReadReplyChildrenResponseDto;
import com.example.cluvrapi.domain.replyChild.entity.MentionInfo;
import com.example.cluvrapi.domain.replyChild.entity.ReplyChild;
import com.example.cluvrapi.domain.replyChild.repository.ReplyChildRepository;
import com.example.cluvrapi.domain.user.entity.User;
import com.example.cluvrapi.domain.user.repository.UserRepository;
import com.example.cluvrapi.global.exception.NoPermissionException;
import com.example.cluvrapi.global.response.ResponseCode;

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

	@Override
	@Transactional(readOnly = true)
	public PageResponseDto<ReadReplyChildrenResponseDto> readReplychildren(long replyId, Pageable pageable) {
		return replyChildRepository.findAllReplyChildrenByParent(replyId, pageable);
	}

	@Override
	@Transactional
	public void updateReplyChild(long userId, long replyChildId, UpdateReplyChildRequestDto dto) {
		ReplyChild replyChild = replyChildRepository.findByIdOrElseThrow(replyChildId);
		User user = userRepository.findByIdOrElseThrow(userId);
		MentionInfo mentionInfo = null;

		if(dto.getMentionedUserId() != null) {
			User mentionedUser = userRepository.findByIdOrElseThrow(dto.getMentionedUserId());
			mentionInfo = new MentionInfo(mentionedUser.getId(), mentionedUser.getName());
		}

		if(!replyChild.getUser().equals(user)) {
			throw new NoPermissionException(ResponseCode.NO_PERMISSION_DELETE,
				ResponseCode.NO_PERMISSION_DELETE.getDefaultMessage());
		}

		replyChild.update(mentionInfo, dto.getContent());
	}
}
