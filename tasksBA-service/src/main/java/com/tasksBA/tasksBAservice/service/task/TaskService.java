package com.tasksBA.tasksBAservice.service.task;

import com.tasksBA.tasksBAservice.dto.TaskDTO;
import com.tasksBA.tasksBAservice.model.Task;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TaskService {
    List<Task> getAll();
    Optional<Task> getTask(Long id);

    void createTask(TaskDTO taskDTO);
    void deleteTask(Task task);

    void editTask(TaskDTO taskDTO);
}
