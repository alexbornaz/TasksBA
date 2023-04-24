package com.tasksBA.tasksBAservice.exceptions.auth;

public class EmailAlreadyExistsException extends Exception{
    public EmailAlreadyExistsException(String message){
        super(message);
    }
}
