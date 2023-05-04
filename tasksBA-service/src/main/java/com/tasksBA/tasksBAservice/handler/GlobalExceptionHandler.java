package com.tasksBA.tasksBAservice.handler;

import com.tasksBA.tasksBAservice.dto.responses.MessageResp;
import com.tasksBA.tasksBAservice.exceptions.ObjectNotValidException;
import com.tasksBA.tasksBAservice.exceptions.UserNotFoundException;
import com.tasksBA.tasksBAservice.exceptions.auth.AuthenticationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ObjectNotValidException.class)
    public ResponseEntity<?> handleException(ObjectNotValidException exception) {
        return ResponseEntity.badRequest().body(exception.getErrorMessages());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> handleException(AuthenticationException exception) {
        return ResponseEntity.status(401).body(new MessageResp(exception.getCause().getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleException(UserNotFoundException exception) {
        return ResponseEntity.unprocessableEntity().body(new MessageResp(exception.getMessage()));
    }


}
