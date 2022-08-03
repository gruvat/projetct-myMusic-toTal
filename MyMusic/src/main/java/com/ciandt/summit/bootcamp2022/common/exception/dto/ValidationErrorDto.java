package com.ciandt.summit.bootcamp2022.common.exception.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ValidationErrorDto {
    private Timestamp timestamp;
    private int status;
    private String error;
    private String message;
    private List<String> errors;
    private String path;
}
