package com.recargapay.app.config.exception;

public class RecordAlreadyExistsException extends RuntimeException {
    public RecordAlreadyExistsException(String message) {
        super(message);
    }
}
