package io.dodn.springboot.common.support;

import io.dodn.springboot.common.support.error.CoreException;
import io.dodn.springboot.common.support.error.ErrorType;
import io.dodn.springboot.common.support.response.ApiResponse;
import io.dodn.springboot.matzip.exception.NotFoundPlaceException;
import io.dodn.springboot.member.exception.NotFoundMemberException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiControllerAdvice {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(CoreException.class)
    public ResponseEntity<ApiResponse<?>> handleCoreException(CoreException e) {
        switch (e.getErrorType().getLogLevel()) {
            case ERROR -> log.error("CoreException : {}", e.getMessage(), e);
            case WARN -> log.warn("CoreException : {}", e.getMessage(), e);
            default -> log.info("CoreException : {}", e.getMessage(), e);
        }
        return new ResponseEntity<>(ApiResponse.error(e.getErrorType(), e.getData()), e.getErrorType().getStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleException(Exception e) {
        log.error("Exception : {}", e.getMessage(), e);
        return new ResponseEntity<>(ApiResponse.error(ErrorType.ERR_1099), ErrorType.ERR_1099.getStatus());
    }

    @ExceptionHandler(NotFoundMemberException.class)
    public ResponseEntity<ApiResponse<?>> handleNotFoundMemberException(NotFoundMemberException e) {
        log.warn("NotFoundMemberException : {}", e.getMessage(), e); // 스택 트레이스 없이 메시지만 로깅
        return new ResponseEntity<>(ApiResponse.error(ErrorType.ERR_1001, e.getMessage()), ErrorType.ERR_1001.getStatus());
    }

    // Spring @Valid 유효성 검사 실패 시 (MethodArgumentNotValidException)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.warn("MethodArgumentNotValidException : {}", e.getMessage()); // 보통 필드별 오류 메시지가 e.getBindingResult()에 있음
        // ErrorType에 VALIDATION_FAILED 와 같은 타입을 정의하고 사용
        // e.getBindingResult()를 파싱하여 더 상세한 오류 메시지를 ApiResponse에 담을 수도 있습니다.
        return new ResponseEntity<>(ApiResponse.error(ErrorType.INVALID_INPUT_VALUE, e.getBindingResult().getAllErrors().get(0).getDefaultMessage()), ErrorType.INVALID_INPUT_VALUE.getStatus());
    }

    // 필수 요청 파라미터 누락 시 (MissingServletRequestParameterException)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<?>> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        log.warn("MissingServletRequestParameterException : {}", e.getMessage());
        // ErrorType에 REQUIRED_PARAMETER_MISSING 과 같은 타입을 정의하고 사용
        return new ResponseEntity<>(ApiResponse.error(ErrorType.MISSING_REQUEST_PARAMETER, e.getParameterName() + " 파라미터가 필요합니다."), ErrorType.MISSING_REQUEST_PARAMETER.getStatus());
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ApiResponse<?>> handleAuthorizationDeniedException(AuthorizationDeniedException e) {
        log.warn("AuthorizationDeniedException : {}", e.getMessage());
        return new ResponseEntity<>(ApiResponse.error(ErrorType.AUTHORIZATION_DENIED), ErrorType.AUTHORIZATION_DENIED.getStatus());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<?>> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("IllegalArgumentException : {}", e.getMessage());
        return new ResponseEntity<>(ApiResponse.error(ErrorType.ERR_1004), ErrorType.ERR_1004.getStatus());
    }

    @ExceptionHandler(NotFoundPlaceException.class)
    public ResponseEntity<ApiResponse<?>> handleNotFoundPlaceException(NotFoundPlaceException e) {
        log.warn("NotFoundPlaceException : {}", e.getMessage());
        return new ResponseEntity<>(ApiResponse.error(ErrorType.ERR_1005), ErrorType.ERR_1005.getStatus());
    }


}
