package com.example.controller;

import com.example.exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController {
    private Logger log = LoggerFactory.getLogger(ExceptionHandlerController.class);

    @ExceptionHandler({
        EmailAlreadyExistsException.class,
            AppBadRequestException.class,
            ItemNotFoundException.class,
            RegionAlreadyExsistException.class,
            TagAlreadyExsistException.class,
            ArticleException.class,
            CategoryAlreadyExistException.class,
            PasswordOrEmailWrongException.class,
            ArticleTypeAlreadyExitsException.class,
            ItemAlreadyExistsException.class,
            ArticleTypeException.class
    })
    public ResponseEntity<?> handlerExc(RuntimeException runtimeException) {
        log.warn(runtimeException.getMessage());
        return ResponseEntity.badRequest().body(runtimeException.getMessage());
    }

    @ExceptionHandler({AppForbiddenException.class})
    public ResponseEntity<?> handleForbidden(RuntimeException e){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }

    @ExceptionHandler({TokenNotValidException.class})
    public ResponseEntity<?> handler(TokenNotValidException e){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }
}
