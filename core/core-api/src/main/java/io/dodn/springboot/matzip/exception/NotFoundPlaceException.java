package io.dodn.springboot.matzip.exception;

public class NotFoundPlaceException extends RuntimeException {
    public NotFoundPlaceException(String message) {
        super(message);
    }
}
