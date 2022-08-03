package com.ciandt.summit.bootcamp2022.common.exception;

import com.ciandt.summit.bootcamp2022.common.exception.dto.ValidationErrorDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Log4j2
@RestControllerAdvice
public class ExceptionHandlerConstraints {
    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public List<ValidationErrorDto> handle(MethodArgumentNotValidException exception) {

        List<ValidationErrorDto> errors = new ArrayList<>();
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();

        fieldErrors.forEach(e -> {
            String message = messageSource.getMessage(e, Locale.US);
            ValidationErrorDto error = new ValidationErrorDto(e.getField(), message);
            errors.add(error);
            log.error(error);
        });

        return errors;
    }
}
