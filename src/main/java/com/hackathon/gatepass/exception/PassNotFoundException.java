package com.hackathon.gatepass.exception;

public class PassNotFoundException extends RuntimeException {
    public PassNotFoundException(String message) {
        super(message);
    }
}
