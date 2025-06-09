package com.example.cluvrapi.domain.applicationForm.service;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.cluvrapi.domain.applicationForm.dto.request.CreateSubmissionFormRequestDto;
import com.example.cluvrapi.domain.applicationForm.dto.request.UpdateSubmissionTemplateRequestDto;
import com.example.cluvrapi.domain.applicationForm.dto.response.CreateSubmissionFormResponseDto;
import com.example.cluvrapi.domain.applicationForm.dto.response.InfoSubmissionFormResponseDto;
import com.example.cluvrapi.domain.applicationForm.entity.SubmissionForm;
import com.example.cluvrapi.domain.applicationForm.repository.SubmissionFormRepository;
import com.example.cluvrapi.domain.club.entity.Club;
import com.example.cluvrapi.domain.club.enums.JoinType;
import com.example.cluvrapi.domain.club.repository.ClubRepository;
import com.example.cluvrapi.domain.common.dto.PageResponseDto;
import com.example.cluvrapi.global.exception.BusinessException;
import com.example.cluvrapi.global.response.ResponseCode;

@Service
@RequiredArgsConstructor
public class SubmissionFormServiceImpl implements SubmissionFormService {

	private final SubmissionFormRepository submissionFormRepository;
	private final ClubRepository clubRepository;

	@Override
	@Transactional
	public CreateSubmissionFormResponseDto createSubmissionForm(
		Long clubId,
		CreateSubmissionFormRequestDto submissionFormRequestDto
	) {
		// 1) Club 조회
		Club findClub = clubRepository.findByIdOrElseThrow(clubId);

		// 2) Club 의 JoinType 이 ProblemForm 인지 검증
		if (findClub.getJoinType() != JoinType.SUBMISSION_FORM) {
			new BusinessException(ResponseCode.INVALID_REQUEST);
		}

		// 3) Form Entity 생성
		SubmissionForm submissionForm = new SubmissionForm(
			submissionFormRequestDto.getSubmissionForm(),
			findClub
		);

		// 4) 저장
		submissionFormRepository.save(submissionForm);

		return CreateSubmissionFormResponseDto.from(submissionForm.getId());
	}

	@Override
	@Transactional(readOnly = true)
	public InfoSubmissionFormResponseDto findSubmissionFormById(Long clubId, Long submissionFormId) {
		return submissionFormRepository.findSubmissionFormById(clubId, submissionFormId);
	}

	@Override
	@Transactional(readOnly = true)
	public PageResponseDto<InfoSubmissionFormResponseDto> findAllSubmissionForm(Long clubId, Pageable pageable) {
		return submissionFormRepository.findAllSubmissionFormById(clubId, pageable);
	}

	@Override
	@Transactional
	public void updateSubmissionTemplate(
		Long clubId,
		Long submissionId,
		UpdateSubmissionTemplateRequestDto updateSubmissionTemplateRequestDto
	) {
		// 1) Submission Form 조회
		SubmissionForm findSubmissionForm = submissionFormRepository.findByClubIdAndSubmissionId(clubId, submissionId)
			.orElseThrow(() ->
				new BusinessException(ResponseCode.NOT_FOUND, "Club 과 Submission Form 이 일치하는 Entity 가 존재하지 않습니다.")
			);

		// 2) Template 수정
		if (updateSubmissionTemplateRequestDto.getSubmissionTemplate() != null) {
			findSubmissionForm.updateSubmissionTemplate(updateSubmissionTemplateRequestDto.getSubmissionTemplate());
		}

	}

	@Override
	@Transactional
	public void deleteSubmissionForm(Long clubId, Long submissionId) {
		// 1) Submission Form 조회
		SubmissionForm findSubmissionForm = submissionFormRepository.findByClubIdAndSubmissionId(clubId, submissionId)
			.orElseThrow(() ->
				new BusinessException(ResponseCode.NOT_FOUND, "Club 과 Submission Form 이 일치하는 Entity 가 존재하지 않습니다.")
			);

		// 2) 삭제 - soft Delete 적용
		submissionFormRepository.delete(findSubmissionForm);

		// 3) 즉시 가입 방식으로 변경
		Club findClub = clubRepository.findByIdOrElseThrow(clubId);
		findClub.changeJoinTypeToSimpleRequest();
	}

}
