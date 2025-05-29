package io.dodn.springboot.common.support.error;

public enum ErrorCode {
    INTERNAL_SERVER_ERROR("E500", "Internal server error occurred"),
    BAD_REQUEST("E400", "Bad request"),
    UNAUTHORIZED("E401", "Unauthorized access"),
    FORBIDDEN("E403", "Forbidden access"),
    CONFLICT("E409", "Conflict occurred"),
    METHOD_NOT_ALLOWED("E405", "Method not allowed"),
    UNSUPPORTED_MEDIA_TYPE("E415", "Unsupported media type"),
    UNPROCESSABLE_ENTITY("E422", "Unprocessable entity"),
    NOT_FOUND("E404", "Resource not found");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
