package com.tasksBA.tasksBAservice.repository;

import com.tasksBA.tasksBAservice.model.Status;
import com.tasksBA.tasksBAservice.model.Task;
import com.tasksBA.tasksBAservice.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    Page<Task> findAllByAssignedTo(User user, Pageable pageable);

    @Query("SELECT t FROM Task  t WHERE (:username IS NULL OR t.assignedTo.username = :username) AND" +
            " (:subject IS NULL OR LOWER(t.subject) LIKE LOWER(CONCAT('%', :subject, '%'))) AND" +
            " (:status IS NULL OR t.status = :status) AND" +
            " (cast( :dueDate as date ) IS NULL OR t.dueDate >cast( :dueDate as date )) ORDER BY t.dueDate DESC")
    Page<Task> findTasksBySearchReq(@Param("username") String username,
                                    @Param("subject") String subject,
                                    @Param("status") Status status,
                                    @Param("dueDate") LocalDate dueDate, Pageable pageable);
}


