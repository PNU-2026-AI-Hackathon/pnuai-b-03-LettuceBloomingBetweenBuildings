package ai.pnu.b03.minifarm.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 400 BAD_REQUEST: 잘못된 요청
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "C001", "잘못된 입력값입니다."),

    // 401 UNAUTHORIZED: 인증되지 않은 사용자
    UNAUTHORIZED_USER(HttpStatus.UNAUTHORIZED, "C002", "인증되지 않은 사용자입니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "C003", "비밀번호가 일치하지 않습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "C004", "유효하지 않은 토큰입니다."),


    // 404 NOT_FOUND: 리소스를 찾을 수 없음
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "C005", "해당 사용자를 찾을 수 없습니다."),

    // 409 CONFLICT: 리소스 충돌 (중복 등)
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "C006", "이미 사용 중인 이메일입니다."),

    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "C007", "존재하지 않는 리소스 또는 API 경로입니다."),

    // 500 INTERNAL_SERVER_ERROR: 서버 내부 에러
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S001", "서버 내부 에러가 발생했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}