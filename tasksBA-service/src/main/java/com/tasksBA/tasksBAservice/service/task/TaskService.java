package com.tasksBA.tasksBAservice.service.task;

import com.tasksBA.tasksBAservice.dto.requests.SearchReq;
import com.tasksBA.tasksBAservice.dto.requests.TaskDTO;
import com.tasksBA.tasksBAservice.exceptions.UserNotFoundException;
import com.tasksBA.tasksBAservice.model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskService {
    List<Task> getAll();

    Task getTask(Long id);

    List<Task> getAssignedTasks(String username) throws UserNotFoundException;

    void createTask(TaskDTO taskDTO) throws UserNotFoundException;

    void deleteTask(Long taskId) throws UserNotFoundException;

    void editTask(TaskDTO taskDTO) throws UserNotFoundException;

    List<Task> searchTasks(SearchReq searchReq);
}
