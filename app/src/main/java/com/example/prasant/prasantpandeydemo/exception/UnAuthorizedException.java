package com.example.prasant.prasantpandeydemo.exception;

public class UnAuthorizedException extends Exception {

    String message;

    public UnAuthorizedException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
