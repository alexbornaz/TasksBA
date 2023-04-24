package com.tasksBA.tasksBAservice.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String subject;
    private Date dueDate;

    private Status status;
    @ManyToOne(optional = false)
    private User assignedTo;

    public Task(String subject, Date dueDate) {
        this.subject = subject;
        this.dueDate = dueDate;
        this.status = Status.NEW;
    }


}
