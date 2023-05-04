package com.tasksBA.tasksBAservice.dto.responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private String username;

    public UserDTO(String username) {
        this.username = username;
    }
}
