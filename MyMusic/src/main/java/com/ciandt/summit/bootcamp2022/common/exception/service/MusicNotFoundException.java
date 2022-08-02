package com.ciandt.summit.bootcamp2022.common.exception.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MusicNotFoundException extends RuntimeException {

    public MusicNotFoundException(String message) {
        super(message);
    }
}
