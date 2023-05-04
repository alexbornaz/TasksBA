package com.tasksBA.tasksBAservice.exceptions.auth;

public class UsernameOrPasswordExistsException extends Exception {
    public UsernameOrPasswordExistsException(String message) {
        super(message);
    }
}
