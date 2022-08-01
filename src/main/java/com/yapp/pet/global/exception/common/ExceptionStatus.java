package com.yapp.pet.global.exception.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor
public enum ExceptionStatus{

	// Common
	RUN_TIME_EXCEPTION(500, "런타임 에러", INTERNAL_SERVER_ERROR),
	NOT_FOUND_EXCEPTION(404, "요청한 리소스가 존재하지 않습니다.", NOT_FOUND),
	INVALID_TYPE_VALUE_EXCEPTION(400, "유효하지 않는 Type의 값입니다. 입력 값을 확인 해주세요.", BAD_REQUEST),
	INVALID_FORMAT_EXCEPTION(400, "유효하지 않는 Type 입니다. Type을 확인 해주세요.", BAD_REQUEST),
	INVALID_INPUT_VALUE_EXCEPTION(400, "유효하지 않는 입력 값입니다.", BAD_REQUEST),
	METHOD_NOT_SUPPORT_EXCEPTION(405, "지원하지 않은 HTTP Method 입니다.", METHOD_NOT_ALLOWED),
	ENTITY_NOT_FOUND_EXCEPTION(500, "해당 엔티티가 존재하지 않습니다.", INTERNAL_SERVER_ERROR),

	//JWT
	AUTHORITY_INFO_NOT_FOUND_EXCEPTION(403, "권한 정보가 없는 토큰입니다.", FORBIDDEN),
	INVALID_JWT_SIGNATURE_EXCEPTION(401,"잘못된 JWT 서명입니다.", UNAUTHORIZED),
	EXPIRED_JWT_TOKEN_EXCEPTION(401, "만료된 JWT 토큰입니다.", UNAUTHORIZED),
	UNSUPPORTED_JWT_TOKEN_EXCEPTION(401, "지원되지 않는 JWT 토큰입니다.", UNAUTHORIZED),
	INVALID_JWT_TOKEN_EXCEPTION(401, "JWT 토큰이 잘못되었습니다. 토큰 존재 여부 및 타입을 확인하세요.", UNAUTHORIZED),
	NOT_REFRESH_TOKEN_EXCEPTION(401, "Refresh Token이 아닙니다.", UNAUTHORIZED),
	NOT_FOUND_TOKEN_EXCEPTION(401, "Token이 존재하지 않습니다.", UNAUTHORIZED),

	//Club
	NOT_PARTICIPATING_CLUB_EXCEPTION(400, "해당 모임에 참여하지 않은 유저입니다.", BAD_REQUEST),
	NOT_HAVING_ANY_PET_EXCEPTION(403, "해당 모임에 참여할 수 있는 애완견이 없습니다", FORBIDDEN),
	NOT_LEADER_EXCEPTION(400, "해당 모임의 방장이 아닙니다.", BAD_REQUEST),

	//Comment

	NOT_DELETE_COMMENT_EXCEPTION(403, "자기 자신의 댓글만을 삭제할 수 있습니다", FORBIDDEN),

	//Pet

	NOT_FOUND_PET_EXCEPTION(400, "삭제할 펫이 없습니다", BAD_REQUEST);

	private final int status;
	private final String message;
	private final HttpStatus httpStatus;

}
