package com.tasksBA.tasksBAservice.service.task;

import com.tasksBA.tasksBAservice.dto.requests.TaskDTO;
import com.tasksBA.tasksBAservice.model.Task;
import com.tasksBA.tasksBAservice.model.User;

import java.util.List;
import java.util.Optional;

public interface TaskService {
    List<Task> getAll();
    Optional<Task> getTask(Long id);
    List<Task> getAssignedTasks(String username);

    void createTask(TaskDTO taskDTO);
    void deleteTask(Task task);

    void editTask(TaskDTO taskDTO);
}
