package com.tasksBA.tasksBAservice.dto.requests;

import com.tasksBA.tasksBAservice.model.Status;
import lombok.Data;


import java.time.LocalDate;

@Data
public class TaskDTO {
    private Long id;
    private String subject;
    private LocalDate dueDate;
    private String assignedTo;
    private Status status;

    public TaskDTO(String subject, LocalDate dueDate, String assignedTo) {
        this.subject = subject;
        this.dueDate = dueDate;
        this.assignedTo = assignedTo;
    }
}


