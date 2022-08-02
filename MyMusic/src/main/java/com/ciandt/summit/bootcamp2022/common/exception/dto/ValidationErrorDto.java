package com.ciandt.summit.bootcamp2022.common.exception.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ValidationErrorDto {

    private String field;
    private String message;

}
