package com.tasksBA.tasksBAservice.dto;

import com.tasksBA.tasksBAservice.model.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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


