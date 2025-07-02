package com.example.cluvrapi.domain.auth.service;

import com.example.cluvrapi.domain.auth.dto.request.CompleteProfileRequestDto;
import com.example.cluvrapi.domain.auth.dto.request.LoginUserRequestDto;
import com.example.cluvrapi.domain.auth.dto.request.SignUpUserRequestDto;
import com.example.cluvrapi.domain.auth.dto.request.SignUpVerifyRequestDto;
import com.example.cluvrapi.domain.auth.dto.request.SocialSignupRequestDto;
import com.example.cluvrapi.domain.auth.dto.response.LoginUserResponseDto;
import com.example.cluvrapi.domain.auth.dto.response.SignUpUserResponseDto;
import com.example.cluvrapi.domain.auth.dto.response.SocialLoginResponseDto;
import com.example.cluvrapi.domain.user.entity.User;

/**
 * 인증 관련 주요 기능(SignUp, Login, Logout)을 제공하는 서비스 인터페이스입니다.
 *
 * @author 정승원
 */
public interface AuthService {

	/**
	 * 설명: 신규 사용자 회원가입을 처리합니다.
	 *
	 * <p>이 메서드는 이메일·전화번호 중복 검사, 비밀번호 확인 절차를 거쳐
	 *       사용자 엔티티와 연관된 카테고리 엔티티를 생성하고 저장합니다.
	 *
	 * @param requestDto 설명: 회원가입에 필요한 사용자 정보(이름, 생년월일, 이메일, 전화번호, 비밀번호 등)
	 * @return SignUpUserResponseDto 설명: 저장된 사용자 정보로 구성된 DTO
	 * @throws BusinessException 설명: 이메일 또는 전화번호 중복, 비밀번호·비밀번호 확인 불일치 등 검증 실패 시 발생
	 */
	void signUp(SignUpUserRequestDto requestDto);

	/**
	 * 설명: 기존 사용자의 로그인 인증을 수행하고 JWT 토큰을 발급합니다.
	 *
	 * <p>이 메서드는 이메일·비밀번호 인증, 탈퇴 여부 확인 후
	 *       액세스 토큰과 리프레시 토큰을 생성하여 반환합니다.
	 *
	 * @param requestDto 설명: 로그인 요청 정보(이메일, 비밀번호)
	 * @return LoginUserResponseDto 설명: 로그인 사용자 정보 및 발급된 토큰(액세스 토큰, 리프레시 토큰)을 포함한 DTO
	 * @throws BusinessException 설명: 잘못된 인증 정보 또는 탈퇴한 사용자 접근 시 발생
	 */
	LoginUserResponseDto login(LoginUserRequestDto requestDto);

	/**
	 * 설명: 액세스 토큰을 블랙리스트에 등록하여 로그아웃을 처리합니다.
	 *
	 * <p>이 메서드는 토큰이 제공되었는지 확인하고, 유효한 토큰일 경우
	 *       남은 만료 시간을 기준으로 Redis 등에 블랙리스트로 등록합니다.
	 *
	 * @param accessToken 설명: 로그아웃할 액세스 토큰 (Bearer 접두어 제외)
	 * @throws BusinessException 설명: 토큰 미제공, 토큰 무효, 블랙리스트 등록 실패 등 처리 중 오류 발생 시
	 */
	void logout(String accessToken);


	/**
	 * 설명: 이메일 인증 코드를 검증하고 회원가입을 완료합니다.
	 *
	 * <p>이 메서드는 Redis에 캐시된 회원가입 요청을 조회하여 인증 코드를 검증하고,
	 * 검증 성공 시 사용자 엔티티와 연관된 카테고리, 클로버 엔티티를 생성하고 저장합니다.
	 *
	 * @param requestDto 설명: 이메일과 인증 코드를 포함한 검증 요청 정보
	 * @return SignUpUserResponseDto 설명: 저장된 사용자 정보로 구성된 DTO
	 * @throws BusinessException 설명: 인증 요청 없음, 만료, 코드 불일치 등의 경우 발생
	 */
	SignUpUserResponseDto completeSignUp(SignUpVerifyRequestDto requestDto);

	SignUpUserResponseDto testSignUp(SignUpUserRequestDto dto);

	SocialLoginResponseDto socialLogin(String idToken);
}
