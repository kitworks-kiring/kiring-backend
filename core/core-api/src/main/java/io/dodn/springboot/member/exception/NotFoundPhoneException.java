package io.dodn.springboot.member.exception;

public class NotFoundPhoneException extends RuntimeException {
    public NotFoundPhoneException(String message) {
        super(message);
    }
}
