package com.example.cluvrapi.domain.join.repository;

import java.util.Optional;

import org.springframework.data.domain.Pageable;

import com.example.cluvrapi.domain.common.dto.PageResponseDto;
import com.example.cluvrapi.domain.join.dto.response.InfoJoinRequestResponseDto;
import com.example.cluvrapi.domain.join.dto.response.MyClubJoinResponseDto;
import com.example.cluvrapi.domain.join.dto.response.MyJoinRequestResponseDto;
import com.example.cluvrapi.domain.join.entity.JoinRequest;
import com.example.cluvrapi.domain.join.entity.JoinRequestAnswer;

/**
 * JoinRequest 관련 커스텀 쿼리 메서드를 정의하는 인터페이스입니다.
 */

public interface JoinRequestRepositoryQuery {

	/**
	 * 설명: 주어진 유저 ID와 클럽 ID에 해당하는 가입 요청이 존재하는지 여부를 확인하는 쿼리문
	 *
	 * @param clubId 클럽 고유 식별자
	 * @param userId 유저 고유 식별자
	 * @return Optional<JoinRequest> 가입 요청
	 * @author sinyoung0403
	 */

	Optional<JoinRequest> findJoinByClubIdAndUserId(Long clubId, Long userId);

	/**
	 * 설명: 특정 클럽에 대한 모든 가입 요청 목록을 페이징하여 조회하는 쿼리문
	 *
	 * @param clubId   클럽 고유 식별자
	 * @param pageable 페이징 정보
	 * @return 가입 요청 목록 페이지
	 * @author sinyoung0403
	 */

	PageResponseDto<MyClubJoinResponseDto> findJoinRequestByClubId(Long clubId, Pageable pageable);

	/**
	 * 설명: 특정 유저가 보낸 모든 가입 요청 목록을 페이징하여 조회하는 쿼리문
	 *
	 * @param userId   유저 고유 식별자
	 * @param pageable 페이징 정보
	 * @return 유저의 가입 요청 목록 페이지
	 * @author sinyoung0403
	 */

	PageResponseDto<MyJoinRequestResponseDto> findMyJoinRequests(Long userId, Pageable pageable);

	/**
	 * 설명: 클럽 ID와 가입 요청 ID에 해당하는 가입 요청 상세 정보를 조회하는 쿼리문
	 *
	 * <p> 가입 양식 응답이 없는 요청도 함께 포함됩니다.
	 *
	 * @param clubId        클럽 고유 식별자
	 * @param joinRequestId 가입 요청 고유 식별자
	 * @return 가입 요청 상세 정보
	 * @author sinyoung0403
	 */

	Optional<InfoJoinRequestResponseDto> findJoinRequestById(Long clubId, Long joinRequestId);

	/**
	 * 설명: 클럽 ID와 가입 요청 ID에 해당하는 가입 요청 응답(Answer)을 조회하는 쿼리문
	 *
	 * @param clubId        클럽 고유 식별자
	 * @param joinRequestId 가입 요청 고유 식별자
	 * @return 가입 요청 응답 정보, 존재하지 않으면 빈 Optional
	 * @author sinyoung0403
	 */

	Optional<JoinRequestAnswer> findJoinRequestAnswerByIdAndClubId(Long clubId, Long joinRequestId);

	/**
	 * 설명: 클럽 ID와 가입 요청 ID에 해당하는 가입 요청 정보를 조회하는 쿼리문
	 *
	 * @param clubId        클럽 고유 식별자
	 * @param joinRequestId 가입 요청 고유 식별자
	 * @return 가입 요청 정보, 존재하지 않으면 빈 Optional
	 * @author sinyoung0403
	 */

	Optional<JoinRequest> joinRequestByIdAndClubId(Long clubId, Long joinRequestId);
}
