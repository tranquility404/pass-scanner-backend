package com.hackathon.gatepass.exception;

public class PassAlreadyVerifiedException extends RuntimeException {
    public PassAlreadyVerifiedException(String message) {
        super(message);
    }
}
