package com.ciandt.summit.bootcamp2022.common.exception.controller;

import com.ciandt.summit.bootcamp2022.common.exception.dto.ValidationErrorDto;
import com.ciandt.summit.bootcamp2022.common.exception.dto.ValidationErrorWithFieldsDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Log4j2
@RestControllerAdvice
public class ExceptionHandlerController {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<ValidationErrorDto> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception, WebRequest request) {

        List<ValidationErrorDto> errors = new ArrayList<>();
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();

        fieldErrors.forEach(e -> {
            String message = messageSource.getMessage(e, Locale.US);
            ValidationErrorWithFieldsDto error = new ValidationErrorWithFieldsDto(
                    LocalDateTime.now(),
                    HttpStatus.BAD_REQUEST.value(),
                    HttpStatus.BAD_REQUEST.getReasonPhrase(),
                    message,
                    request.getDescription(false).replace("uri=", ""),
                    e.getField());

            errors.add(error);
        });

        log.error("Invalid fields on controller's method body");
        return errors;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorDto handleHttpMessageNotReadableException(
            HttpMessageNotReadableException exception, WebRequest request) {
        log.error("Invalid body.");
        return new ValidationErrorDto(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Request with invalid format. Must be JSON.",
                request.getDescription(false).replace("uri=", ""));
    }

}
