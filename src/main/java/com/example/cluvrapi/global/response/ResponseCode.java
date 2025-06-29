package com.example.cluvrapi.global.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import org.springframework.http.HttpStatus;

@Getter
@ToString
@AllArgsConstructor
public enum ResponseCode {

	/* JSON 결과 */
	OK(HttpStatus.OK, "요청 처리 성공"),
	CREATED(HttpStatus.CREATED, "요청 처리 성공"),
	NO_CONTENT(HttpStatus.NO_CONTENT, "요청 처리 성공"),
	FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "요청 처리 실패"),
	DB_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "요청 DB 처리 실패"),
	VALID_FAIL(HttpStatus.BAD_REQUEST, "유효성 검증에 실패하였습니다."),
	NOT_FOUND(HttpStatus.NOT_FOUND, "NOT FOUND"),

	// cognito
	INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Cognito 가입 중 오류가 발생하였습니다."),
	ALREADY_COGNITO(HttpStatus.BAD_REQUEST, "이미 존재하는 Cognito 계정입니다."),
	NO_COGNITO_SUB(HttpStatus.NOT_FOUND, "cognito에 sub가 존재하지 않습니다."),

	/* 인증, 인가 */
	LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "아이디(로그인 이메일) 또는 비밀번호가 잘못 되었습니다. 아이디와 비밀번호를 정확히 입력해 주세요."),
	AUTH_ANNOTATION_USER_MISMATCH(HttpStatus.UNAUTHORIZED, "@Auth와 AuthUser 타입은 함께 사용되어야 합니다."),
	AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),
	ACCESS_DENIED(HttpStatus.FORBIDDEN, "Access Denied."),
	WITHDRAWN_USER_ACCESS(HttpStatus.FORBIDDEN, "탈퇴한 유저는 접근할 수 없습니다."),
	TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
	TOKEN_BLACKLISTED(HttpStatus.FORBIDDEN, "다시 로그인 해주세요."),
	AUTH_REQUIRED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),
	EMAIL_NOT_VERIFIED(HttpStatus.NOT_FOUND, "이메일 인증이 완료되지 않았습니다."),
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 이메일의 사용자를 찾을 수 없습니다."),
	/* 서버 */
	UNKNOWN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 오류"),

	/* 데이터 검증 */
	INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
	/* GEM */
	GEM_NOT_ENOUGH(HttpStatus.BAD_REQUEST, "포인트가 부족합니다."),
	/* CLOVER */
	CLOVER_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 유저의 클로버가 존재하지 않습니다"),
	CLOVER_POST_LIMIT(HttpStatus.BAD_REQUEST, "클로버는 100 아래로만 걸 수 있습니다."),
	/* 통계 */
	STAT_NOT_ENOUGH(HttpStatus.NOT_FOUND, "해당 유저의 통계가 존재하지 않습니다 ."),
	/* Board, Reply */
	SELF_REACTION_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "자신이 작성한 것에 좋아요/싫어요를 선택할 수 없습니다."),
	BOARD_REPLY_MISMATCH(HttpStatus.BAD_REQUEST, "게시글의 해당하는 댓글이 아닙니다."),
	NO_PERMISSION_DELETE(HttpStatus.NOT_ACCEPTABLE, "현재 유저는 이 게시물을 삭제할 수 있는 권한이 없습니다."),
	ALREADY_SELECTED(HttpStatus.BAD_REQUEST, "채택은 각 게시물 당 한 개만 가능합니다. 이미 채택을 하셨습니다."),
	CANNOT_SELECT_OWN_REPLY(HttpStatus.BAD_REQUEST, "게시글 작성자와 댓글 작성자가 동일 인물이므로 채택할 수 없습니다."),
	REPLY_NOT_MATCHED_WITH_BOARD(HttpStatus.BAD_REQUEST, "채택한 댓글은 해당 게시물 소속의 댓글이 아닙니다.");

	private final HttpStatus status;
	private final String defaultMessage;
}
