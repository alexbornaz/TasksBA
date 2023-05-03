package com.tasksBA.tasksBAservice.dto.requests;

import com.tasksBA.tasksBAservice.model.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
@Getter
@AllArgsConstructor
public class SearchReq {
    private String assignedTo;
    private LocalDate dueDate;
    private Status status;
    private String subject;
}
