package com.example.cluvrapi.domain.join.repository;

import com.example.cluvrapi.domain.common.repository.BaseRepository;
import com.example.cluvrapi.domain.join.entity.JoinRequest;

/**
 * JoinRequest 엔티티의 기본 CRUD 및 커스텀 쿼리 기능을 제공하는 리포지토리입니다.
 */

public interface JoinRequestRepository extends BaseRepository<JoinRequest, Long>, JoinRequestRepositoryQuery {

}
