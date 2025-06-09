package io.dodn.springboot.common.support.error;

import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

public enum ErrorType {

    FAILED_KAKAO(HttpStatus.NOT_FOUND, ErrorCode.NOT_FOUND, "Failed to retrieve Kakao token.", LogLevel.ERROR),
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_INPUT_VALUE,"입력값이 올바르지 않습니다.", LogLevel.WARN),
    MISSING_REQUEST_PARAMETER(HttpStatus.BAD_REQUEST, ErrorCode.BAD_REQUEST,"필수 요청 파라미터가 누락되었습니다.", LogLevel.WARN),
    AUTHORIZATION_DENIED(HttpStatus.FORBIDDEN, ErrorCode.FORBIDDEN, "접근 권한이 없습니다.", LogLevel.WARN),
    NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND, ErrorCode.NOT_FOUND, "가입되지 않은 사용자 입니다.", LogLevel.INFO),
    OAUTH_LOGIN_FAILED(HttpStatus.UNAUTHORIZED, ErrorCode.UNAUTHORIZED,"OAuth2 로그인에 실패했습니다.", LogLevel.WARN),
    DEFAULT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_SERVER_ERROR,"서버 내부 오류가 발생했습니다.", LogLevel.ERROR);


    private final HttpStatus status;

    private final ErrorCode code;

    private final String message;

    private final LogLevel logLevel;

    ErrorType(HttpStatus status, ErrorCode code, String message, LogLevel logLevel) {

        this.status = status;
        this.code = code;
        this.message = message;
        this.logLevel = logLevel;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public ErrorCode getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public LogLevel getLogLevel() {
        return logLevel;
    }

}
