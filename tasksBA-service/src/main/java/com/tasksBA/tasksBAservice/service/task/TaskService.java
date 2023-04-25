package com.tasksBA.tasksBAservice.service.task;

import com.tasksBA.tasksBAservice.dto.TaskDTO;
import com.tasksBA.tasksBAservice.model.Task;

import java.util.Optional;

public interface TaskService {

    Optional<Task> getTask(Long id);

    void createTask(TaskDTO taskDTO);
    void deleteTask(Task task);
}
