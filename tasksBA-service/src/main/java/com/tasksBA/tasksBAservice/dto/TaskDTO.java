package com.tasksBA.tasksBAservice.dto;

import com.tasksBA.tasksBAservice.model.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
public class TaskDTO {
    private String subject;
    private Date dueDate;
    private String assignedTo;

    public TaskDTO(String subject, Date dueDate, String assignedTo) {
        this.subject = subject;
        this.dueDate = dueDate;
        this.assignedTo = assignedTo;
    }
}


