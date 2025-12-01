package com.hackathon.gatepass.exception;

public class DuplicatePassCodeException extends RuntimeException {
    public DuplicatePassCodeException(String message) {
        super(message);
    }
}
