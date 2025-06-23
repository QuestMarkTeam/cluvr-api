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

		// 멘션 대상이 존재하면 정보 생성
		MentionInfo mentionInfo = null;
		if (dto.getMentionId() != null) {
			User mention = userRepository.findByIdOrElseThrow(dto.getMentionId());
			mentionInfo = new MentionInfo(mention.getId(), mention.getName());
		}

		ReplyChild replyChild = new ReplyChild(user, dto.getContent(), parentReply, mentionInfo);
		return replyChildRepository.save(replyChild).getId();
	}

	@Override
	@Transactional(readOnly = true)
	public PageResponseDto<ReadReplyChildrenResponseDto> readReplyChildren(long replyId, Pageable pageable) {
		return replyChildRepository.findAllReplyChildrenByParent(replyId, pageable);
	}

	@Override
	@Transactional
	public void updateReplyChild(long userId, long replyChildId, UpdateReplyChildRequestDto dto) {
		User user = userRepository.findByIdOrElseThrow(userId);
		ReplyChild replyChild = replyChildRepository.findByIdOrElseThrow(replyChildId);

		// 권한 검증
		validateUserPermission(user, replyChild.getUser());

		MentionInfo mentionInfo;
		if (dto.getMentionedUserId().isPresent()) {
			Long mentionedUserId = dto.getMentionedUserId().get();
			if (mentionedUserId != null) {
				// 다른 mention으로 변경
				User mentionedUser = userRepository.findByIdOrElseThrow(mentionedUserId);
				mentionInfo = new MentionInfo(mentionedUser.getId(), mentionedUser.getName());
			} else {
				// mention은 null로 변경
				mentionInfo = null; // 언급 제거
			}
		} else {
			// mention 변경 x
			mentionInfo = replyChild.getMention();
		}

		replyChild.update(mentionInfo, dto.getContent());
	}

	@Override
	@Transactional
	public void deleteReplyChild(long userId, long replyChildId) {
		User user = userRepository.findByIdOrElseThrow(userId);
		ReplyChild replyChild = replyChildRepository.findByIdOrElseThrow(replyChildId);

		// 권한 검증
		validateUserPermission(user, replyChild.getUser());

		replyChildRepository.delete(replyChild);
	}

	/**
	 * 댓글 작성자와 요청자의 권한이 일치하는지 검증
	 */
	private void validateUserPermission(User requestUser, User resourceOwner) {
		if (!requestUser.equals(resourceOwner)) {
			throw new NoPermissionException(ResponseCode.NO_PERMISSION_DELETE,
				ResponseCode.NO_PERMISSION_DELETE.getDefaultMessage());
		}
	}
}
