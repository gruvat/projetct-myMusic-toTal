package com.ciandt.summit.bootcamp2022.exception;

public class MissingCredentialsException extends Exception {

    public MissingCredentialsException() {
    }

    public MissingCredentialsException(String message) {
        super(message);
    }
    
}