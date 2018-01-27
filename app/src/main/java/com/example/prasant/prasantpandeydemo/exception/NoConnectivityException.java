package com.example.prasant.prasantpandeydemo.exception;

import java.io.IOException;



public class NoConnectivityException extends IOException {

    @Override
    public String getMessage() {
        return "No Internet connection";
    }

}
