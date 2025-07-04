package io.dodn.springboot.common.support.response;

import io.dodn.springboot.common.support.error.ErrorMessage;
import io.dodn.springboot.common.support.error.ErrorType;

public class ApiResponse<S> {

    private final ResultType result;

    private final S data;

    private final ErrorMessage error;

    private ApiResponse(ResultType result, S data, ErrorMessage error) {
        this.result = result;
        this.data = data;
        this.error = error;
    }

    public static ApiResponse<Void> success() {
        return new ApiResponse<>(ResultType.SUCCESS, null, null);
    }

    public static <S> ApiResponse<S> success(S data) {
        return new ApiResponse<>(ResultType.SUCCESS, data, null);
    }

    public static <T> ApiResponse<T> error(ErrorType error) {
        return new ApiResponse<>(ResultType.ERROR, null, new ErrorMessage(error));
    }

    public static <T,E> ApiResponse<T> error(ErrorType error, E errorData) {
        return new ApiResponse<>(ResultType.ERROR, null, new ErrorMessage(error, errorData));
    }

    public ResultType getResult() {
        return result;
    }

    public Object getData() {
        return data;
    }

    public ErrorMessage getError() {
        return error;
    }

}
