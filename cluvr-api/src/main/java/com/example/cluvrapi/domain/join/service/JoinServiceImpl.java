package com.example.cluvrapi.domain.join.service;

import java.util.Map;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.cluvrapi.domain.applicationForm.repository.ProblemFormRepository;
import com.example.cluvrapi.domain.applicationForm.repository.SubmissionFormRepository;
import com.example.cluvrapi.domain.club.entity.Club;
import com.example.cluvrapi.domain.club.enums.JoinType;
import com.example.cluvrapi.domain.club.repository.ClubRepository;
import com.example.cluvrapi.domain.common.dto.PageResponseDto;
import com.example.cluvrapi.domain.join.dto.request.CreateJoinRequestByCodeRequestDto;
import com.example.cluvrapi.domain.join.dto.request.CreateJoinRequestDto;
import com.example.cluvrapi.domain.join.dto.request.UpdateJoinRequestDto;
import com.example.cluvrapi.domain.join.dto.response.CreateJoinRequestByCodeResponseDto;
import com.example.cluvrapi.domain.join.dto.response.CreateJoinResponseDto;
import com.example.cluvrapi.domain.join.dto.response.InfoJoinRequestResponseDto;
import com.example.cluvrapi.domain.join.dto.response.MyClubJoinResponseDto;
import com.example.cluvrapi.domain.join.dto.response.MyJoinRequestResponseDto;
import com.example.cluvrapi.domain.join.entity.JoinRequest;
import com.example.cluvrapi.domain.join.entity.JoinRequestAnswer;
import com.example.cluvrapi.domain.join.enums.FormFieldType;
import com.example.cluvrapi.domain.join.enums.JoinStatus;
import com.example.cluvrapi.domain.join.repository.JoinRequestAnswerRepository;
import com.example.cluvrapi.domain.join.repository.JoinRequestRepository;
import com.example.cluvrapi.domain.notification.enums.NotiTargetType;
import com.example.cluvrapi.domain.notification.enums.NotificationType;
import com.example.cluvrapi.domain.notification.event.NotificationEvent;
import com.example.cluvrapi.domain.notification.event.NotificationProducer;
import com.example.cluvrapi.domain.user.entity.User;
import com.example.cluvrapi.domain.user.repository.UserRepository;
import com.example.cluvrapi.global.exception.BusinessException;
import com.example.cluvrapi.global.response.ResponseCode;

/**
 * 가입 요청(Join Request)에 대한 서비스 인터페이스 구현체입니다.
 */

@Service
@RequiredArgsConstructor
public class JoinServiceImpl implements JoinService {

	private final UserRepository userRepository;
	private final ClubRepository clubRepository;
	private final JoinRequestRepository joinRequestRepository;
	private final JoinRequestAnswerRepository joinRequestAnswerRepository;
	private final ProblemFormRepository problemFormRepository;
	private final SubmissionFormRepository submissionFormRepository;
	private final NotificationProducer notificationProducer;
	private final JoinRedisService joinRedisService;

	/**
	 * 상수 선언
	 */
	private static final String INVITE_CODE_KEY_PREFIX = "ic:";

	@Override
	@Transactional
	public CreateJoinResponseDto createJoin(Long userId, Long clubId, CreateJoinRequestDto joinRequestDto) {
		// 1) 검증
		validateJoinRequest(clubId, userId);
		User findUser = userRepository.findByIdOrElseThrow(userId);
		Club findClub = clubRepository.findByIdOrElseThrow(clubId);

		// 2) 현재 Club 의 가입 방식과 가입 신청의 가입 방식이 같은지 확인
		if (findClub.getJoinType() != joinRequestDto.getJoinType()) {
			throw new BusinessException(ResponseCode.INVALID_REQUEST, "가입방식과 신청 가입방식이 일치하지 않습니다.");
		}

		// 3) 가입 방식에 따른 초기 상태값 설정
		JoinStatus initJoinStatus = determineJoinStatus(joinRequestDto.getJoinType());

		// 4) Join Request Entity 생성 및 저장
		JoinRequest joinRequest = new JoinRequest(findUser, findClub, initJoinStatus, joinRequestDto.getJoinType());

		joinRequestRepository.save(joinRequest);

		// 5) 가입 양식에 따라, 분리된 로직 실행
		switch (findClub.getJoinType()) {
			case DIRECT_JOIN -> processDirectJoin();
			case SIMPLE_REQUEST -> processSimpleRequest();
			case SUBMISSION_FORM -> processFormSubmission(clubId, joinRequest, joinRequestDto.getAnswer());
			case PROBLEM_FORM -> processFormProblem(clubId, joinRequest, joinRequestDto.getAnswer());
		}

		// 6) 클럽장에게 알림 전송
		User clubOwner = findClub.getUser();
		if (!clubOwner.getId().equals(findUser.getId())) { // 자기 자신이 아닌 경우에만
			String content = String.format("'%s'님이 '%s' 클럽에 가입 신청을 했습니다.", findUser.getName(), findClub.getName());

			NotificationEvent event = NotificationEvent.from(
				clubOwner.getId(),                 // 클럽장에게
				NotificationType.JOIN_REQUEST,
				content,
				NotiTargetType.CLUB,
				clubId
			);

			notificationProducer.send(event);
		}

		return CreateJoinResponseDto.from(joinRequest.getId());
	}

	@Override
	@Transactional(readOnly = true)
	public PageResponseDto<MyClubJoinResponseDto> findJoinRequestByClubId(Long clubId, Pageable pageable) {
		return joinRequestRepository.findJoinRequestByClubId(clubId, pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public PageResponseDto<MyJoinRequestResponseDto> findMyJoinRequests(Long userId, Pageable pageable) {
		return joinRequestRepository.findMyJoinRequests(userId, pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public InfoJoinRequestResponseDto findJoinRequestById(Long clubId, Long joinRequestId) {
		return joinRequestRepository.findJoinRequestById(clubId, joinRequestId);
	}

	@Override
	@Transactional
	public void updateJoinRequestAnswer(Long clubId, Long joinRequestId, UpdateJoinRequestDto updateJoinRequestDto) {
		JoinRequestAnswer findJoinRequestAnswer = joinRequestRepository.findJoinRequestAnswerByIdAndClubId(clubId,
				joinRequestId)
			.orElseThrow(
				() -> new BusinessException(ResponseCode.NOT_FOUND)
			);

		findJoinRequestAnswer.updateAnswer(updateJoinRequestDto.getAnswer());
	}

	@Override
	@Transactional
	public void cancelJoinRequest(Long clubId, Long joinRequestId) {
		// 1) Request 찾기
		JoinRequest findJoinRequest = joinRequestRepository.joinRequestByIdAndClubId(clubId, joinRequestId)
			.orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND));

		// 2) Join Status 를 Cancel 로 수정
		findJoinRequest.updateJoinStatus();

		// 3) JoinRequest Answer 이 존재한다면, 삭제
		joinRequestRepository.findJoinRequestAnswerByIdAndClubId(clubId, joinRequestId)
			.ifPresent(joinRequestAnswerRepository::delete);
	}

	@Override
	public CreateJoinRequestByCodeResponseDto createJoinRequestByInviteCode(Long userId,
		CreateJoinRequestByCodeRequestDto createJoinRequestByCodeRequestDto) {
		// 1) Key 확인
		String key = INVITE_CODE_KEY_PREFIX + createJoinRequestByCodeRequestDto.getInviteCode();
		Map<Object, Object> codeData = joinRedisService.entries(key);

		// 2) data 검증
		if (codeData.isEmpty()) {
			throw new BusinessException(ResponseCode.INVALID_REQUEST, "존재하지 않는 초대코드 입니다.");
		}

		Long clubId = Long.valueOf(codeData.get("clubId").toString());
		Club findClub = clubRepository.findByIdOrElseThrow(clubId);
		User findUser = userRepository.findByIdOrElseThrow(userId);

		// 3) 클럽 가입 유무 검증
		validateJoinRequest(clubId, userId);

		// 4) Join Request Entity 생성 및 저장
		JoinRequest joinRequest = new JoinRequest(findUser, findClub, JoinStatus.PENDING, JoinType.INVITE_CODE);
		joinRequestRepository.save(joinRequest);

		return CreateJoinRequestByCodeResponseDto.from(joinRequest.getId());
	}

	/**
	 * 설명: Join request 가 유효한 신청인지 확인하는 검증 메서드
	 *
	 * <p> 중복신청과 이미 가입된 유저인지 확인한다.
	 *
	 * @param clubId {설명: 클럽 고유 식별자}
	 * @param userId {설명: 유저 고유 식별자}
	 * @throws BusinessException {400 BadRequest}
	 * @author sinyoung0403
	 */
	private void validateJoinRequest(Long clubId, Long userId) {
		// 1. 중복 신청 조회
		boolean alreadyRequested = joinRequestRepository.existsJoinByClubIdAndUserId(clubId, userId);
		if (alreadyRequested != false) {
			throw new BusinessException(ResponseCode.INVALID_REQUEST, "이미 가입 신청한 클럽입니다.");
		}

		// 2. 이미 가입된 유저인지 조회
		// 추후 추가 예정

		// 3. 클럽원 다 찼는지 확인
		// 추후 추가 예정
	}

	/**
	 * 설명: JoinStatus 설정해주는 메서드
	 *
	 * @param joinType {설명: 가입 방식}
	 * @return JoinStatus {설명: 가입 상태}
	 * @author sinyoung0403
	 */
	private JoinStatus determineJoinStatus(JoinType joinType) {
		return joinType == JoinType.DIRECT_JOIN ? JoinStatus.APPROVED : JoinStatus.PENDING;
	}

	/**
	 * 설명: Join Type 이 DIRECT_JOIN 일 경우의 분기 메서드
	 *
	 * <p> 추후에 수정 예정
	 *
	 * @author sinyoung0403
	 */
	private void processDirectJoin() {
		// 클럽 유저 추가 Service 메서드 호출
	}

	/**
	 * 설명: Join Type 이 SIMPLE_REQUEST 일 경우의 분기 메서드
	 *
	 * <p> 현재까지는 어떠한 작업도 필요없다
	 *
	 * @author sinyoung0403
	 */
	private void processSimpleRequest() {
		// 추후 필요한 로직 추가할 예정 - 현재까지는 어떠한 작업도 필요없다.
		// 신청만하고. -> 클럽장이 승인
		// 조회 -> 신청양식 없이 조회.
	}

	/**
	 * 설명: Join Type 이 Submission 일 경우의 분기 메서드
	 *
	 * <p> Form 의 가입 양식이 클럽과 일치해야한다.
	 *
	 * @param clubId      {설명: 클럽 고유 식별자}
	 * @param joinRequest {설명: JoinRequest Entity}
	 * @param answers     {설명: 가입 양식 혹은 문제에 대한 답변}
	 * @throws BusinessException {400 BadRequest}
	 * @author sinyoung0403
	 */
	private void processFormSubmission(Long clubId, JoinRequest joinRequest, String answers) {
		if (answers == null && answers.isEmpty()) {
			throw new BusinessException(ResponseCode.INVALID_REQUEST);
		}

		// 1) Form 의 id 값 추출
		Long submissionFormId = submissionFormRepository.findSubmissionFormIdByClubId(clubId);

		// 2) Entity 생성
		JoinRequestAnswer joinRequestAnswer = new JoinRequestAnswer(joinRequest, submissionFormId,
			FormFieldType.SUBMISSION, answers);

		// 3) 저장
		joinRequestAnswerRepository.save(joinRequestAnswer);

		// 4) 알림
		User applicant = joinRequest.getUser();
		User clubOwner = joinRequest.getClub().getUser();
		if (!clubOwner.getId().equals(applicant.getId())) {
			String content = String.format("'%s'님이 제출 양식을 통해 가입 신청을 했습니다.", applicant.getName());

			NotificationEvent event = NotificationEvent.from(
				clubOwner.getId(),
				NotificationType.SUBMISSION_FORM,
				content,
				NotiTargetType.CLUB,
				clubId
			);

			notificationProducer.send(event);
		}
	}

	/**
	 * 설명: Join Type 이 Problem 일 경우의 분기 메서드
	 *
	 * <p> Active 한 문제 양식일 경우만 가입신청이 가능하다.
	 *
	 * @param clubId      {설명: 클럽 고유 식별자}
	 * @param joinRequest {설명: JoinRequest Entity}
	 * @param answers     {설명: 가입 양식 혹은 문제에 대한 답변}
	 * @throws BusinessException {400 BadRequest}
	 * @author sinyoung0403
	 */

	private void processFormProblem(Long clubId, JoinRequest joinRequest, String answers) {
		if (answers == null && answers.isEmpty()) {
			throw new BusinessException(ResponseCode.INVALID_REQUEST);
		}

		// 1) Form id 값 추출 - Active 한 Id 값을 가져온다.
		Long problemFormId = problemFormRepository.findActiveProblemFormIdByClubId(clubId)
			.orElseThrow(() -> new BusinessException(ResponseCode.INVALID_REQUEST, "잘못된 요청입니다."));

		// 2) Entity 생성
		JoinRequestAnswer joinRequestAnswer = new JoinRequestAnswer(joinRequest, problemFormId, FormFieldType.PROBLEM,
			answers);

		// 3. 저장
		joinRequestAnswerRepository.save(joinRequestAnswer);

		// 4) 알림
		User applicant = joinRequest.getUser();
		User clubOwner = joinRequest.getClub().getUser();
		if (!clubOwner.getId().equals(applicant.getId())) {
			String content = String.format("'%s'님이 문제 양식을 통해 가입 신청을 했습니다.", applicant.getName());

			NotificationEvent event = NotificationEvent.from(
				clubOwner.getId(),
				NotificationType.PROBLEM_FORM,
				content,
				NotiTargetType.CLUB,
				clubId
			);

			notificationProducer.send(event);
		}
	}
}
