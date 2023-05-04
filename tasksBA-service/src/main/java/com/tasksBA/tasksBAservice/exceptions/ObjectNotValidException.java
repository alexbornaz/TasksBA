package com.tasksBA.tasksBAservice.exceptions;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Getter
public class ObjectNotValidException extends RuntimeException{
    private final Set<String> errorMessages;

    public ObjectNotValidException(Set<String> errorMessages) {
        this.errorMessages = errorMessages;
        log.warn("Invalid attempt to login with error messages:");
        errorMessages.forEach(log::error);
    }
}
