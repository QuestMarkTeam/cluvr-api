package com.example.cluvrapi.domain.join.repository;

import com.example.cluvrapi.domain.common.repository.BaseRepository;
import com.example.cluvrapi.domain.join.entity.JoinRequest;

public interface JoinRequestRepository extends BaseRepository<JoinRequest, Long>, JoinRequestRepositoryCustom {

}
