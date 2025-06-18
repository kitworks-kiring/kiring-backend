package io.dodn.springboot.common.support.error;

import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

public enum ErrorType {

    FAILED_KAKAO(HttpStatus.NOT_FOUND, ErrorCode.NOT_FOUND, "Failed to retrieve Kakao token.", LogLevel.ERROR),
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_INPUT_VALUE, "입력값이 올바르지 않습니다.", LogLevel.WARN),
    MISSING_REQUEST_PARAMETER(HttpStatus.BAD_REQUEST, ErrorCode.BAD_REQUEST, "필수 요청 파라미터가 누락되었습니다.", LogLevel.WARN),
    AUTHORIZATION_DENIED(HttpStatus.FORBIDDEN, ErrorCode.FORBIDDEN, "접근 권한이 없습니다.", LogLevel.WARN),
    OAUTH_LOGIN_FAILED(HttpStatus.UNAUTHORIZED, ErrorCode.UNAUTHORIZED, "OAuth2 로그인에 실패했습니다.", LogLevel.WARN),

    ERR_1001(HttpStatus.NOT_FOUND, ErrorCode.NOT_FOUND, "등록되지 않은 사용자", LogLevel.ERROR),
    ERR_1002(HttpStatus.NOT_FOUND, ErrorCode.NOT_FOUND, "필수 항목 동의 누락", LogLevel.ERROR),
    ERR_1003(HttpStatus.NOT_FOUND, ErrorCode.NOT_FOUND, "휴대전화 번호 존재하지않음", LogLevel.ERROR),
    ERR_1004(HttpStatus.BAD_REQUEST, ErrorCode.BAD_REQUEST, "올바른 요청이 아닙니다.", LogLevel.ERROR),
    ERR_1005(HttpStatus.NOT_FOUND, ErrorCode.NOT_FOUND, "존재하지 않는 공간입니다", LogLevel.ERROR),
    ERR_1006(HttpStatus.BAD_REQUEST, ErrorCode.BAD_REQUEST, "이미 쪽지를 보냈습니다.", LogLevel.ERROR),
    ERR_1007(HttpStatus.BAD_REQUEST, ErrorCode.BAD_REQUEST, "파일이 비어있습니다.", LogLevel.ERROR),
    ERR_1008(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_SERVER_ERROR, "데이터 삽입중 오류가 발생했습니다.", LogLevel.ERROR),
    ERR_1009(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_SERVER_ERROR, "리프레시 토큰이 만료되었습니다.", LogLevel.ERROR),
    ERR_1010(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_SERVER_ERROR, "유효하지 않은 리프레시 토큰.", LogLevel.ERROR),
    ERR_1099(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다.", LogLevel.ERROR);


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
