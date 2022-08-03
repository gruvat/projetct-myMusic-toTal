package com.ciandt.summit.bootcamp2022.common.exception.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ValidationErrorWithFieldsDto extends ValidationErrorDto {

    private String field;

    public ValidationErrorWithFieldsDto(LocalDateTime timestamp, Integer status,
                                        String error, String message, String path, String field) {
        super(timestamp, status, error, message, path);
        this.field = field;
    }
}
