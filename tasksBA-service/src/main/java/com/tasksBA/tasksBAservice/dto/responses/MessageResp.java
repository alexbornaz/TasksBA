package com.tasksBA.tasksBAservice.dto.responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageResp {
    private boolean success;
    private String message;

    public MessageResp(String message) {
        this.message = message;
    }

    public MessageResp(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public MessageResp() {
    }
}
