package com.example.prasant.prasantpandeydemo.exception;

public class MaxRetryException extends Exception {
    String message;

    public MaxRetryException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
