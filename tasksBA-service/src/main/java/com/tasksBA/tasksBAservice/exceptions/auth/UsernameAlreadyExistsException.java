package com.tasksBA.tasksBAservice.exceptions.auth;

public class UsernameAlreadyExistsException extends Exception {
    public UsernameAlreadyExistsException(String message) {
        super(message);
    }
}
