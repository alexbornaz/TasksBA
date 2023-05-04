package com.tasksBA.tasksBAservice.dto.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class SignUpReq {

    @NotNull(message = "username must not be null")
    @NotEmpty(message = "username must not be empty")
    private String username;

    @Email(message = "Email is not valid")
    private String email;

    @NotNull(message = "password must not be null")
    @NotEmpty(message = "password must not be empty")
    private String password;

}
