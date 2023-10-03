package ru.practicum.shareit.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    public String validationException(ValidationException exception) throws JsonProcessingException {
        log.debug("Получен статус 400 BAD_REQUEST {}", exception.getMessage(), exception);
        return new ObjectMapper().writeValueAsString(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public String notFoundException(NotFoundException exception) throws JsonProcessingException {
        log.debug("Получен статус 404 NOT_FOUND {}", exception.getMessage(), exception);
        return new ObjectMapper().writeValueAsString(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DuplicateException.class)
    public String duplicateException(DuplicateException exception) throws JsonProcessingException {
        log.debug("Получен статус 409 CONFLICT {}", exception.getMessage(), exception);
        return new ObjectMapper().writeValueAsString(exception.getMessage());
    }
}
