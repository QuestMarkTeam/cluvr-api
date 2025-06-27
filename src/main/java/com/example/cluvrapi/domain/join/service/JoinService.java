package com.example.cluvrapi.domain.join.service;

import org.springframework.data.domain.Pageable;

import com.example.cluvrapi.domain.common.dto.PageResponseDto;
import com.example.cluvrapi.domain.join.dto.request.CreateJoinRequestByCodeRequestDto;
import com.example.cluvrapi.domain.join.dto.request.CreateJoinRequestDto;
import com.example.cluvrapi.domain.join.dto.request.UpdateJoinRequestDto;
import com.example.cluvrapi.domain.join.dto.response.CreateJoinRequestByCodeResponseDto;
import com.example.cluvrapi.domain.join.dto.response.CreateJoinResponseDto;
import com.example.cluvrapi.domain.join.dto.response.InfoJoinRequestResponseDto;
import com.example.cluvrapi.domain.join.dto.response.MyClubJoinResponseDto;
import com.example.cluvrapi.domain.join.dto.response.MyJoinRequestResponseDto;
import com.example.cluvrapi.global.annotation.IsClubAdmin;
import com.example.cluvrapi.global.exception.BusinessException;

/**
 * 가입 요청(Join Request)에 대한 비즈니스 로직을 정의하는 서비스 인터페이스입니다.
 */

public interface JoinService {

	/**
	 * 설명: 가입 신청을 하는 메서드
	 *
	 * <p>{가입 양식 별로 가입 신청 처리}
	 *
	 * @param userId         {설명: 로그인한 유저의 고유 식별자}
	 * @param clubId         {설명: 클럽 고유 식별자}
	 * @param joinRequestDto {설명: Join Request 에 필요한 정보}
	 * @return CreateJoinResponseDto {설명: Join Request ID 반환}
	 * @throws BusinessException {404 NOT_FOUND}
	 * @throws BusinessException {INVALID_REQUEST: 400 BAD_REQUEST}
	 * @author sinyoung0403
	 */

	CreateJoinResponseDto createJoin(Long userId, Long clubId, CreateJoinRequestDto joinRequestDto);

	/**
	 * 설명: 클럽의 고유 식별자를 통해 Join Request 조회하는 메서드
	 *
	 * <p> 클럽 운영자를 기준으로 조회하는 메서드
	 *
	 * @param userId   {설명: 유저 고유 식별자}
	 * @param clubId   {설명: 클럽 고유 식별자}
	 * @param pageable {설명: 페이징 객체}
	 * @return PageResponseDto<MyClubJoinResponseDto> {Join Request 정보 페이징 객체 -  클랜 운영진 기준}
	 * @author sinyoung0403
	 */

	@IsClubAdmin
	PageResponseDto<MyClubJoinResponseDto> findJoinRequestByClubId(Long userId, Long clubId, Pageable pageable);

	/**
	 * 설명: 유저의 고유 식별자를 통해 Join Request 조회하는 메서드
	 *
	 * <p> 클럽을 신청한 자를 기준으로 조회하는 메서드
	 *
	 * @param userId   {설명: 유저 고유 식별자}
	 * @param pageable {설명: 페이징 객체}
	 * @return PageResponseDto<MyJoinRequestResponseDto> {Join Request 정보 페이징 객체 - 가입 유저 기준}
	 * @author sinyoung0403
	 */

	PageResponseDto<MyJoinRequestResponseDto> findMyJoinRequests(Long userId, Pageable pageable);

	/**
	 * 설명: Join Request 의 고유 식별자를 통해 조회하는 메서드
	 *
	 * <p> 단일 Request 를 조회하는 메서드이다. 이때, Answer 또한 조회되며 존재하지 않을 시 "null"로 반환된다.
	 *
	 * @param userId        {설명: 유저 고유 식별자}
	 * @param clubId        {설명: 클럽 고유 식별자}
	 * @param joinRequestId {설명: Join Request 고유 식별자}
	 * @return 가입 요청 상세 정보
	 * @author sinyoung0403
	 */

	InfoJoinRequestResponseDto findJoinRequestById(Long userId, Long clubId, Long joinRequestId);

	/**
	 * 설명: Join Request 를 수정하는 메서드
	 *
	 * @param userId               {설명: 유저 고유 식별자}
	 * @param clubId               {설명: 클럽 고유 식별자}
	 * @param joinRequestId        {설명: Join Request 고유 식별자}
	 * @param updateJoinRequestDto {설명: Join Request 에 필요한 정보 }
	 * @throws BusinessException {ResponseCode.NOT_FOUND}
	 * @author sinyoung0403
	 */

	void updateJoinRequestAnswer(Long userId, Long clubId, Long joinRequestId,
		UpdateJoinRequestDto updateJoinRequestDto);

	/**
	 * 설명: Join Request Status 를 Cancel 상태로 변경해주는 메서드
	 *
	 * <p> Join Type 이 Form Type 일 경우, Cancel 과 동시에 Answer Data 를 Soft Deleted 처리한다.
	 *
	 * @param userId        {설명: 유저 고유 식별자}
	 * @param clubId        {설명: 클럽 고유 식별자}
	 * @param joinRequestId {설명: Join Request 고유 식별자}
	 * @throws BusinessException {ResponseCode.NOT_FOUND}
	 * @author sinyoung0403
	 */

	void cancelJoinRequest(Long userId, Long clubId, Long joinRequestId);

	/**
	 * 설명: 초대 코드를 기반으로 클럽 가입 요청을 생성하는 메서드
	 *
	 * <p> 유효한 초대 코드인 경우에만 가입 요청이 생성되며, 중복 요청 방지 및 클럽 상태 검증 로직이 포함될 수 있다.
	 *
	 * @param userId                            {설명: 유저 고유 식별자}
	 * @param createJoinRequestByCodeRequestDto {설명: 초대 코드 및 요청 관련 정보가 담긴 DTO}
	 * @return CreateJoinRequestByCodeResponseDto {설명: 가입 요청 생성 결과 응답 DTO}
	 * @throws BusinessException {ResponseCode.INVALID_REQUEST, ResponseCode.NOT_FOUND 등}
	 * @author sinyoung0403
	 */

	CreateJoinRequestByCodeResponseDto createJoinRequestByInviteCode(Long userId,
		CreateJoinRequestByCodeRequestDto createJoinRequestByCodeRequestDto);
}
