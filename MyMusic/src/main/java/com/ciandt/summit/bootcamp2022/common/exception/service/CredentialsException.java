package com.ciandt.summit.bootcamp2022.common.exception.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
@Log4j2
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class CredentialsException extends RuntimeException {

    public CredentialsException(String message) {
        super(message + " \uD83D\uDEAB");
        log.error("\uD83D\uDEAB " + message);
    }
    
}
