package com.tasksBA.tasksBAservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class SignUpReq {
    private String username;
    private String email;
    private String password;

}
