package com.ciandt.summit.bootcamp2022.common.exception.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Log4j2
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PlaylistNotFoundException extends RuntimeException {

    public PlaylistNotFoundException(String message) {
        super(message + " \uD83D\uDE41");
        log.error("\uD83D\uDE41 " + message);
    }
}
