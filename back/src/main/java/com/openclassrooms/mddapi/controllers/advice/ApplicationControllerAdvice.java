package com.openclassrooms.mddapi.controllers.advice;

import com.openclassrooms.mddapi.dto.ErrorEntity;
import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class ApplicationControllerAdvice {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationControllerAdvice.class);

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ExpiredJwtException.class})
    public @ResponseBody ErrorEntity handleExpiredJwtException(ExpiredJwtException exception){
        logger.error("Expired JWT: {}", exception.getMessage());
        return new ErrorEntity(HttpStatus.BAD_REQUEST.value(), "Expired JWT: " + exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({EntityNotFoundException.class})
    public @ResponseBody ErrorEntity handleEntityNotFoundException(EntityNotFoundException exception){
        logger.error("Entity not found: {}", exception.getMessage());
        return new ErrorEntity(HttpStatus.BAD_REQUEST.value(), "Entity not found: " + exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ConstraintViolationException.class})
    public @ResponseBody ErrorEntity handleConstraintViolationException(ConstraintViolationException exception){
        logger.error("Validation error occurred");
        StringBuilder errorMessage = new StringBuilder();
        for (ConstraintViolation<?> violation : exception.getConstraintViolations()) {
            errorMessage.append(violation.getMessage()).append("; ");
        }
        return new ErrorEntity(HttpStatus.BAD_REQUEST.value(), "Validation error: " + errorMessage.toString());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler({Exception.class})
    public @ResponseBody ErrorEntity handleGeneralException(Exception exception){
        logger.error("An error occurred: {}", exception.getMessage());
        return new ErrorEntity(HttpStatus.CONFLICT.value(), "An error occurred: " + exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({DataIntegrityViolationException.class})
    public @ResponseBody ErrorEntity handleDataIntegrityViolationException(DataIntegrityViolationException exception){
        logger.error("Data integrity violation: {}", exception.getMessage());

        String errorMessage = exception.getMessage();

        if (errorMessage.contains("users.email")) {
            return new ErrorEntity(HttpStatus.BAD_REQUEST.value(), "Email already used");
        } else if (errorMessage.contains("users.username")) {
            return new ErrorEntity(HttpStatus.BAD_REQUEST.value(), "Username already used");
        } else {
            return new ErrorEntity(HttpStatus.BAD_REQUEST.value(), "Data integrity violation: " + exception.getMessage());
        }
    }

}
