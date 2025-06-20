package com.example.cluvrapi.domain.gem.repository;

import com.example.cluvrapi.domain.common.repository.BaseRepository;
import com.example.cluvrapi.domain.gem.entity.GemLog;

public interface GemLogRepository extends BaseRepository<GemLog, Long>, GemLogRepositoryQuery {
}
