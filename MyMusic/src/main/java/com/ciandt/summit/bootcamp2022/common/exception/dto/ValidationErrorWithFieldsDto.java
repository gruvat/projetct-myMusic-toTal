package com.ciandt.summit.bootcamp2022.common.exception.dto;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ValidationErrorWithFieldsDto extends ValidationErrorDto {

    private List<String> errors;

    public ValidationErrorWithFieldsDto(LocalDateTime timestamp, Integer status,
                                        String error, String message, String path,
                                        List<String> errors) {
        super(timestamp, status, error, message, path);
        this.errors = errors;
    }
}
