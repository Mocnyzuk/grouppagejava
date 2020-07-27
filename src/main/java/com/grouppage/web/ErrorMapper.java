package com.grouppage.web;

import com.grouppage.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorMapper {

    @ExceptionHandler({
            AccessDeniedException.class,
            AccessTokenExpired.class
    })
    public ResponseEntity<String> accessDenied(Exception ex){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

    @ExceptionHandler({
            ConversationNotFoundException.class,
            GroupNotFoundException.class,
            ParticipantNotFountException.class
    })
    public ResponseEntity<String> notFoundException(Exception ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler({
            WrongCredentialsException.class,
            WrongDataPostedException.class,
            AuthenticationException.class,
            BadCredentialsException.class
    })
    public ResponseEntity<String> wrongDataPosted(Exception ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
