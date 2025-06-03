package io.dodn.springboot.common.support.error;

import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

public enum ErrorType {

    DEFAULT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_SERVER_ERROR, "An unexpected error has occurred.", LogLevel.ERROR),
    NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND, ErrorCode.NOT_FOUND, "Member not found.", LogLevel.WARN),
    FAILED_KAKAO(HttpStatus.NOT_FOUND, ErrorCode.NOT_FOUND, "Failed to retrieve Kakao token.", LogLevel.ERROR);

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
