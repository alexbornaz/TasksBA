package com.tasksBA.tasksBAservice.service.task;

import com.tasksBA.tasksBAservice.dto.requests.SearchReq;
import com.tasksBA.tasksBAservice.dto.requests.TaskDTO;
import com.tasksBA.tasksBAservice.exceptions.UserNotFoundException;
import com.tasksBA.tasksBAservice.model.Task;
import org.springframework.data.domain.Page;

public interface TaskService {
    Page<Task> getAll(int page);

    Task getTask(Long id);

    Page<Task> getAssignedTasks(String username, int page) throws UserNotFoundException;

    void createTask(TaskDTO taskDTO) throws UserNotFoundException;

    void deleteTask(Long taskId) throws UserNotFoundException;

    void editTask(TaskDTO taskDTO) throws UserNotFoundException;

    Page<Task> searchTasks(SearchReq searchReq, int page);
}
