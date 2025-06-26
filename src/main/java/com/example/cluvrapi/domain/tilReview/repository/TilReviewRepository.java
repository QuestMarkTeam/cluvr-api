package com.example.cluvrapi.domain.tilReview.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import com.example.cluvrapi.domain.common.repository.BaseRepository;
import com.example.cluvrapi.domain.tilReview.entity.TilReview;

public interface TilReviewRepository extends BaseRepository<TilReview, Long>, TilReviewRepositoryQuery {

}

