package com.example.cluvrapi.domain.point.repository;

import com.example.cluvrapi.domain.common.repository.BaseRepository;
import com.example.cluvrapi.domain.point.entity.PointLog;

public interface PointLogRepository extends BaseRepository<PointLog, Long>, PointLogRepositoryQuery {
}
