package com.example.cluvrapi.domain.applicationForm.service;

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
			new BusinessException(ResponseCode.INVALID_REQUEST);
		}

		// 3) Form Entity 생성
		ProblemForm problemForm = new ProblemForm(
			problemFormRequestDto.getProblemTemplate(),
			problemFormRequestDto.getSubmissionInstructions(),
			problemFormRequestDto.getGradingCriteria(),
			findClub
		);

		// 4) 저장
		problemRepository.save(problemForm);

		return CreateProblemFormResponseDto.from(problemForm.getId());
	}

	@Override
	public InfoProblemFormResponseDto findProblemFormById(Long clubId, Long problemId) {
		return problemRepository.findProblemFormById(clubId, problemId);
	}

	@Override
	public PageResponseDto<InfoProblemFormResponseDto> findAllProblemForm(Long clubId, Pageable pageable) {
		return problemRepository.findByProblemFormAllById(clubId, pageable);
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

		// 2) Template 수정
		if (updateProblemFormRequestDto.getProblemTemplate() != null) {
			findProblemForm.updateProblemForm(updateProblemFormRequestDto.getProblemTemplate());
		}

		// 3) SubmissionInstructions 수정
		if (updateProblemFormRequestDto.getSubmissionInstructions() != null) {
			findProblemForm.updateSubmissionInstructions(updateProblemFormRequestDto.getSubmissionInstructions());
		}

		// 4) GradingCriteria 수정
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

		// 3) 비활성화
		findProblemForm.deactivate();
		// 1) 권한 검증
		ClubMember findClubMember = clubMemberRepository.findByClubIdAndUserId(clubId, userId).orElseThrow(
			() -> new BusinessException(ResponseCode.INVALID_REQUEST, "해당하는 멤버가 존재하지 않습니다.")
		);
		clubValidator.validateOwnerRole(findClubMember.getClubMemberRole());
	}
}
