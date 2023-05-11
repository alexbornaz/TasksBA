package com.tasksBA.tasksBAservice.dto.responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageResp {

    private String message;


    public MessageResp(String message) {
        this.message = message;
    }

    public MessageResp() {
    }
}
