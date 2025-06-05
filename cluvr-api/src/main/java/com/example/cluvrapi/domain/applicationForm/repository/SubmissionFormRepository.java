package com.example.cluvrapi.domain.applicationForm.repository;

import java.util.List;

import com.example.cluvrapi.domain.applicationForm.entity.SubmissionForm;
import com.example.cluvrapi.domain.club.entity.Club;
import com.example.cluvrapi.domain.common.repository.BaseRepository;

public interface SubmissionFormRepository
	extends BaseRepository<SubmissionForm, Long>, SubmissionFormRepositoryCustom {

	List<SubmissionForm> club(Club club);
}
