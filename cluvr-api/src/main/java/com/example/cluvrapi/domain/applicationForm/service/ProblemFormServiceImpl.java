package com.example.cluvrapi.domain.applicationForm.service;

import java.util.Optional;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.cluvrapi.domain.applicationForm.dto.request.CreateProblemFormRequestDto;
import com.example.cluvrapi.domain.applicationForm.dto.request.UpdateProblemFormRequestDto;
import com.example.cluvrapi.domain.applicationForm.dto.response.CreateProblemFormResponseDto;
import com.example.cluvrapi.domain.applicationForm.dto.response.InfoProblemFormResponseDto;
import com.example.cluvrapi.domain.applicationForm.entity.ProblemForm;
import com.example.cluvrapi.domain.applicationForm.repository.ProblemFormRepository;
import com.example.cluvrapi.domain.club.entity.Club;
import com.example.cluvrapi.domain.club.enums.JoinType;
import com.example.cluvrapi.domain.club.repository.ClubRepository;
import com.example.cluvrapi.domain.clubMember.entity.ClubMember;
import com.example.cluvrapi.domain.clubMember.repository.ClubMemberRepository;
import com.example.cluvrapi.domain.common.dto.PageResponseDto;
import com.example.cluvrapi.domain.common.validator.ClubValidator;
import com.example.cluvrapi.global.exception.BusinessException;
import com.example.cluvrapi.global.response.ResponseCode;

@Service
@RequiredArgsConstructor
public class ProblemFormServiceImpl implements ProblemFormService {

	private final ClubRepository clubRepository;
	private final ProblemFormRepository problemRepository;
	private final ClubValidator clubValidator;
	private final ClubMemberRepository clubMemberRepository;

	@Override
	@Transactional
	public CreateProblemFormResponseDto createProblemForm(Long userId,
		Long clubId, CreateProblemFormRequestDto problemFormRequestDto) {
		// 1) Club 조회
		Club findClub = clubRepository.findByIdOrElseThrow(clubId);

		// 2) 권한 검증
		ClubMember findClubMember = clubMemberRepository.findByClubIdAndUserId(clubId, userId).orElseThrow(
			() -> new BusinessException(ResponseCode.INVALID_REQUEST, "해당하는 멤버가 존재하지 않습니다.")
		);
		clubValidator.validateOwnerRole(findClubMember.getClubMemberRole());

		// 3) Club 의 JoinType 이 ProblemForm 인지 검증
		if (findClub.getJoinType() != JoinType.PROBLEM_FORM) {
			throw new BusinessException(ResponseCode.INVALID_REQUEST);
		}

		// 4) Form Entity 생성
		ProblemForm problemForm = new ProblemForm(
			problemFormRequestDto.getProblemTemplate(),
			problemFormRequestDto.getSubmissionInstructions(),
			problemFormRequestDto.getGradingCriteria(),
			true,
			findClub
		);

		// 5) 활성화된 문제 양식을 비활성화로 바꾸어준다.
		Optional<ProblemForm> formOpt = problemRepository.findActiveProblemFormByClubId(clubId);
		formOpt.ifPresent(
			form -> {
				form.deactivate();
			}
		);

		// 6) 저장
		problemRepository.save(problemForm);

		return CreateProblemFormResponseDto.from(problemForm.getId());
	}

	@Override
	@Transactional(readOnly = true)
	public InfoProblemFormResponseDto findProblemFormById(Long clubId, Long problemId) {
		return problemRepository.findProblemFormById(clubId, problemId);
	}

	@Override
	@Transactional(readOnly = true)
	public PageResponseDto<InfoProblemFormResponseDto> findAllProblemForm(Long clubId, Pageable pageable) {
		return problemRepository.findByProblemFormAllById(clubId, pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public InfoProblemFormResponseDto findActiveProblemFormByClubId(Long clubId) {
		ProblemForm activeProblemForm = problemRepository.findActiveProblemFormByClubId(clubId).orElseThrow(
			() -> new BusinessException(ResponseCode.INVALID_REQUEST, "활성화된 문제양식이 없습니다.")
		);
		return InfoProblemFormResponseDto.from(activeProblemForm);
	}

	@Override
	@Transactional
	public void updateProblemForm(Long userId, Long clubId,
		Long problemFormId, UpdateProblemFormRequestDto updateProblemFormRequestDto) {

		// 1) 권한 검증
		ClubMember findClubMember = clubMemberRepository.findByClubIdAndUserId(clubId, userId).orElseThrow(
			() -> new BusinessException(ResponseCode.INVALID_REQUEST, "해당하는 멤버가 존재하지 않습니다.")
		);
		clubValidator.validateOwnerRole(findClubMember.getClubMemberRole());

		// 2) Problem Form 조회
		ProblemForm findProblemForm = problemRepository.findByClubIdAndProblemFormId(clubId, problemFormId)
			.orElseThrow(() ->
				new BusinessException(ResponseCode.NOT_FOUND, "Club 과 Problem Form 이 일치하는 Entity 가 존재하지 않습니다.")
			);

		// 3) Template 수정
		if (updateProblemFormRequestDto.getProblemTemplate() != null) {
			findProblemForm.updateProblemForm(updateProblemFormRequestDto.getProblemTemplate());
		}

		// 4) SubmissionInstructions 수정
		if (updateProblemFormRequestDto.getSubmissionInstructions() != null) {
			findProblemForm.updateSubmissionInstructions(updateProblemFormRequestDto.getSubmissionInstructions());
		}

		// 5) GradingCriteria 수정
		if (updateProblemFormRequestDto.getGradingCriteria() != null) {
			findProblemForm.updateGradingCriteria(updateProblemFormRequestDto.getGradingCriteria());
		}

	}

	@Override
	@Transactional
	public void deleteProblem(Long userId, Long clubId, Long problemFormId) {

		// 1) 권한 검증
		ClubMember findClubMember = clubMemberRepository.findByClubIdAndUserId(clubId, userId).orElseThrow(
			() -> new BusinessException(ResponseCode.INVALID_REQUEST, "해당하는 멤버가 존재하지 않습니다.")
		);
		clubValidator.validateOwnerRole(findClubMember.getClubMemberRole());

		// 2) Problem Form 조회
		ProblemForm findProblemForm = problemRepository.findByClubIdAndProblemFormId(clubId, problemFormId)
			.orElseThrow(() ->
				new BusinessException(ResponseCode.NOT_FOUND, "Club 과 Problem Form 이 일치하는 Entity 가 존재하지 않습니다.")
			);

		// 3) 삭제 - soft Delete 적용
		problemRepository.delete(findProblemForm);

		// 4) 비활성화
		findProblemForm.deactivate();
	}

	@Override
	@Transactional
	public void changeActivationState(Long userId, Long clubId, Long problemFormId, Boolean active) {
		// 1) 권한 검증
		ClubMember findClubMember = clubMemberRepository.findByClubIdAndUserId(clubId, userId).orElseThrow(
			() -> new BusinessException(ResponseCode.INVALID_REQUEST, "해당하는 멤버가 존재하지 않습니다.")
		);
		clubValidator.validateOwnerRole(findClubMember.getClubMemberRole());

		// 2) 문제양식 조회 및 권한 검증
		ProblemForm findProblemForm = problemRepository.findByIdOrElseThrow(problemFormId);

		if (findProblemForm.getIsActive() == active) {
			throw new BusinessException(ResponseCode.INVALID_REQUEST, "이미 해당 상태입니다.");
		}

		if (!findProblemForm.getClub().getId().equals(clubId)) {
			throw new BusinessException(ResponseCode.INVALID_REQUEST, "클럽과 문제양식의 소속이 일치하지 않습니다.");
		}

		// 3) 활성화 및 비활성화
		if (active) {
			// 활성화를 한다고 할 경우, 기존 문제양식 비활성화
			Optional<ProblemForm> formOpt = problemRepository.findActiveProblemFormByClubId(clubId);
			formOpt.ifPresent(
				form -> {
					form.deactivate();
				}
			);
			findProblemForm.activate();
		} else { // 비활성화
			findProblemForm.deactivate();
		}
	}
}
