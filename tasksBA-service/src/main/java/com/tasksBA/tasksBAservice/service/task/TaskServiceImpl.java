package com.tasksBA.tasksBAservice.service.task;

import com.tasksBA.tasksBAservice.dto.requests.SearchReq;
import com.tasksBA.tasksBAservice.dto.requests.TaskDTO;
import com.tasksBA.tasksBAservice.exceptions.UserNotFoundException;
import com.tasksBA.tasksBAservice.model.Status;
import com.tasksBA.tasksBAservice.model.Task;
import com.tasksBA.tasksBAservice.model.User;
import com.tasksBA.tasksBAservice.repository.TaskRepository;
import com.tasksBA.tasksBAservice.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;


@Service
@Slf4j
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final UserService userService;

    public TaskServiceImpl(TaskRepository taskRepository, UserService userService) {
        this.taskRepository = taskRepository;
        this.userService = userService;
    }


    @Override
    public List<Task> getAll() {
        return taskRepository.findAll(Sort.by(Sort.Direction.DESC, "dueDate"));
    }

    @Override
    public Task getTask(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Could not fetch the task with id: " + id));
    }

    @Override
    public List<Task> getAssignedTasks(String username) throws UserNotFoundException {
        User assignedToUser = userService.getUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User does not exist"));
        try {
            return taskRepository.findAllByAssignedToOrderByDueDateDesc(assignedToUser);
        } catch (Exception e) {
            log.error("Could not fetch the tasks for: {}", username, e);
            throw new RuntimeException("Could not fetch the tasks for: " + username);
        }
    }

    @Override
    public void createTask(TaskDTO taskDTO) throws UserNotFoundException {
        User user = userService.getUserByUsername(taskDTO.getAssignedTo())
                .orElseThrow(() -> new UserNotFoundException("User not found for username " + taskDTO.getAssignedTo()));
        try {
            Task task = new Task(taskDTO.getSubject(), taskDTO.getDueDate(), user);
            task.setStatus(taskDTO.getStatus());
            taskRepository.save(task);
            user.addTaskToAssigned(task);
            userService.saveUser(user);
        } catch (Exception e) {
            log.error("Error creating task: {}", e.getMessage(), e);
            throw new RuntimeException("Something went wrong, could not create the task");
        }
    }


    @Override
    public void deleteTask(Long taskId) throws UserNotFoundException {
        Task taskToBeDeleted = taskRepository.findById(taskId)
                .orElseThrow(() -> new NoSuchElementException("Task with id " + taskId + " does not exist!"));

        User user = userService.getUserByUsername(taskToBeDeleted.getAssignedTo().getUsername())
                .orElseThrow(() -> new UserNotFoundException("There is no user with username " + taskToBeDeleted.getAssignedTo().getUsername()));

        user.removeTaskFromAssigned(taskToBeDeleted);
        userService.saveUser(user);

        taskRepository.deleteById(taskId);

    }

    @Override
    public void editTask(TaskDTO taskDTO) throws UserNotFoundException {
        String errMsg = "Task couldn't be edited,something went wrong";
        Task task = taskRepository.findById(taskDTO.getId())
                .orElseThrow(() -> new NoSuchElementException(errMsg));
        User user = userService.getUserByUsername(taskDTO.getAssignedTo())
                .orElseThrow(() -> new UserNotFoundException(errMsg));
        try {
            task.setSubject(taskDTO.getSubject());
            task.setDueDate(taskDTO.getDueDate());
            task.setStatus(taskDTO.getStatus());
            task.setAssignedTo(user);
            taskRepository.save(task);
        } catch (Exception e) {
            log.error("Couldn't edit the task with id: {}", task.getId(), e);
            throw new RuntimeException("Something went wrong, editing the task with id: " + task.getId());
        }
    }

    @Override
    public List<Task> searchTasks(SearchReq searchReq) {
        String username = searchReq.getAssignedTo();
        String subject = searchReq.getSubject();
        Status status = searchReq.getStatus();
        LocalDate dueDate = searchReq.getDueDate();
        return taskRepository.findTasksBySearchReq(username, subject, status, dueDate);
    }
}
