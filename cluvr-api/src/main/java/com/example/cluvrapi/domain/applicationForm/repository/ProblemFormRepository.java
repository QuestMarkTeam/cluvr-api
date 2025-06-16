package com.example.cluvrapi.domain.applicationForm.repository;

import com.example.cluvrapi.domain.applicationForm.entity.ProblemForm;
import com.example.cluvrapi.domain.common.repository.BaseRepository;

public interface ProblemFormRepository
	extends BaseRepository<ProblemForm, Long>, ProblemFormRepositoryQuery {

}
