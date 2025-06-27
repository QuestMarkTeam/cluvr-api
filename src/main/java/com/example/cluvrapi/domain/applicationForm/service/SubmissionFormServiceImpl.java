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
		Long userId,
		Long clubId, CreateSubmissionFormRequestDto submissionFormRequestDto
	) {
		// 1) Club 조회
		Club findClub = clubRepository.findByIdOrElseThrow(clubId);

		// 2) Club 의 JoinType 이 SubmissionForm 인지 검증
		if (findClub.getJoinType() != JoinType.SUBMISSION_FORM) {
			throw new BusinessException(ResponseCode.INVALID_REQUEST, "가입방식이 `SubmissionForm`과 동일하지 않습니다.");
		}

		// 3) 이미 존재하고 있다면, 생성 불가
		if (submissionFormRepository.findSubmissionFormByClubId(clubId).isPresent()) {
			throw new BusinessException(ResponseCode.INVALID_REQUEST, "이미 양식 존재합니다.");
		}

		// 4) Form Entity 생성
		SubmissionForm submissionForm = new SubmissionForm(
			submissionFormRequestDto.getSubmissionForm(),
			findClub
		);

		// 5) 저장
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
		Long userId,
		Long clubId,
		Long submissionFormId, UpdateSubmissionTemplateRequestDto updateSubmissionTemplateRequestDto
	) {

		// 1) Submission Form 조회
		SubmissionForm findSubmissionForm = submissionFormRepository.findByClubIdAndSubmissionId(clubId,
				submissionFormId)
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
	public void deleteSubmissionForm(Long userId, Long clubId, Long submissionFormId) {

		// 1) Submission Form 조회
		SubmissionForm findSubmissionForm = submissionFormRepository.findByClubIdAndSubmissionId(clubId,
				submissionFormId)
			.orElseThrow(() ->
				new BusinessException(ResponseCode.NOT_FOUND, "Club 과 Submission Form 이 일치하는 Entity 가 존재하지 않습니다.")
			);

		// 2) 삭제
		submissionFormRepository.delete(findSubmissionForm);
	}
}
