package com.tasksBA.tasksBAservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String subject;
    private LocalDate dueDate;

    private Status status;
    @ManyToOne(optional = false)
    @JoinColumn(name = "assigned_to_username", referencedColumnName = "username")
    private User assignedTo;

    public Task(String subject, LocalDate dueDate, User assignedTo) {
        this.subject = subject;
        this.dueDate = dueDate;
        this.status = Status.NEW;
        this.assignedTo = assignedTo;
    }


}
