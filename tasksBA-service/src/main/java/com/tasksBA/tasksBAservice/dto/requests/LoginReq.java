package com.tasksBA.tasksBAservice.dto.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginReq {
    @NotNull(message = "username must not be null")
    @NotEmpty(message = "username must not be empty")
    private String username;
    @NotNull(message = "password must not be null")
    @NotEmpty(message = "password must not be empty")
    private String password;
}
