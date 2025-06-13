package com.example.cluvrapi.domain.club.repository;

import com.example.cluvrapi.domain.club.entity.Club;
import com.example.cluvrapi.domain.common.repository.BaseRepository;

/**
 * 클럽 엔티티에 대한 기본 CRUD 및 사용자 정의 쿼리 기능을 제공하는 리포지토리 인터페이스
 *
 * <p> BaseRepository 를 상속받아 기본적인 데이터베이스 조작 기능을 이용할 수 있으며,
 * ClubRepositoryQuery 인터페이스를 통해 복잡한 쿼리 기능도 확장 가능합니다.
 *
 * @author sinyoung0403
 */

public interface ClubRepository extends BaseRepository<Club, Long>, ClubRepositoryCustom {

}
