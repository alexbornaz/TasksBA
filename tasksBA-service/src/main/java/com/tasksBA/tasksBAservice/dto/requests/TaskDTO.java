package com.tasksBA.tasksBAservice.dto.requests;

import com.tasksBA.tasksBAservice.model.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
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

    public TaskDTO(String subject, LocalDate dueDate, String assignedTo, Status status) {
        this.subject = subject;
        this.dueDate = dueDate;
        this.assignedTo = assignedTo;
        this.status = status;
    }
}


