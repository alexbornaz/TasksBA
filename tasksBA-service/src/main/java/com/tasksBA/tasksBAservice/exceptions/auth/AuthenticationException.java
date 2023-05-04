package com.tasksBA.tasksBAservice.exceptions.auth;

public class AuthenticationException extends Exception {
    public AuthenticationException(Exception cause) {
        super(cause);
    }

    public AuthenticationException(String message) {
        super(message);
    }
}
