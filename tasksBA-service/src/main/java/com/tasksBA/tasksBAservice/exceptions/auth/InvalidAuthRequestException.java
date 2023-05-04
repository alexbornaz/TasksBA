package com.tasksBA.tasksBAservice.exceptions.auth;

public class InvalidAuthRequestException extends Exception{
    public InvalidAuthRequestException(String message) {
        super(message);
    }
}
