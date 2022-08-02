package com.ciandt.summit.bootcamp2022.common.exception.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Log4j2
@ResponseStatus(HttpStatus.NO_CONTENT)
public class MusicsAndArtistsNotFoundException extends RuntimeException {
    public MusicsAndArtistsNotFoundException() {
        log.info("No content");
    }
}
