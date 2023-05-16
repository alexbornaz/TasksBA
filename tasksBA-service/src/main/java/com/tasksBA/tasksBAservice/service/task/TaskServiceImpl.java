package com.tasksBA.tasksBAservice.service.task;

import com.tasksBA.tasksBAservice.dto.requests.SearchReq;
import com.tasksBA.tasksBAservice.dto.requests.TaskDTO;
import com.tasksBA.tasksBAservice.exceptions.UserNotFoundException;
import com.tasksBA.tasksBAservice.model.Status;
import com.tasksBA.tasksBAservice.model.Task;
import com.tasksBA.tasksBAservice.model.User;
import com.tasksBA.tasksBAservice.repository.TaskRepository;
import com.tasksBA.tasksBAservice.service.EmailService;
import com.tasksBA.tasksBAservice.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.NoSuchElementException;


@Service
@Slf4j
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final UserService userService;
    private final EmailService emailService;

    public TaskServiceImpl(TaskRepository taskRepository, UserService userService, EmailService emailService) {
        this.taskRepository = taskRepository;
        this.userService = userService;
        this.emailService = emailService;
    }


    @Override
    public Page<Task> getAll(int page) {
        int actualPage = page - 1;
        Pageable pageable = PageRequest.of(actualPage, 4, Sort.by(Sort.Direction.DESC, "dueDate"));
        Page<Task> tasks = taskRepository.findAll(pageable);
        long totalCount = tasks.getTotalElements();
        return new PageImpl<>(tasks.getContent(), pageable, totalCount);
    }

    @Override
    public Task getTask(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Could not fetch the task with id: " + id));
    }

    @Override
    public Page<Task> getAssignedTasks(String username, int page) throws UserNotFoundException {
        int actualPage = page - 1;
        User assignedToUser = userService.getUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User does not exist"));
        try {
            Pageable pageable = PageRequest.of(actualPage, 4, Sort.by(Sort.Direction.DESC, "dueDate"));
            Page<Task> tasks = taskRepository.findAllByAssignedTo(assignedToUser, pageable);
            long totalCount = tasks.getTotalElements();
            return new PageImpl<>(tasks.getContent(), pageable, totalCount);
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
            addTaskToAssigned(user, task);
            taskEmailDecider(task, user, false);
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
        try {
            removeTaskFromAssigned(user, taskToBeDeleted);
            taskRepository.deleteById(taskId);
        } catch (Exception e) {
            log.error("Failed deleting task with id {}", taskId, e);
            throw new RuntimeException("Something went wrong, could not delete the task");
        }

    }

    private void removeTaskFromAssigned(User user, Task task) {
        user.removeTaskFromAssigned(task);
        userService.saveUser(user);
    }

    private void addTaskToAssigned(User user, Task task) {
        user.addTaskToAssigned(task);
        userService.saveUser(user);
    }

    @Override
    public void editTask(TaskDTO taskDTO) throws UserNotFoundException {
        String errMsg = "Task couldn't be edited,something went wrong";
        Task task = taskRepository.findById(taskDTO.getId())
                .orElseThrow(() -> new NoSuchElementException(errMsg));
        User user = task.getAssignedTo();
        boolean unchangedAssignedTo = user.getUsername().equals(taskDTO.getAssignedTo());
        try {
            task.setSubject(taskDTO.getSubject());
            task.setDueDate(taskDTO.getDueDate());
            task.setStatus(taskDTO.getStatus());
            if (!unchangedAssignedTo) {
                User newUser = userService.getUserByUsername(taskDTO.getAssignedTo())
                        .orElseThrow(() -> new UserNotFoundException(errMsg));
                removeTaskFromAssigned(user, task);
                addTaskToAssigned(newUser, task);
                task.setAssignedTo(newUser);
            }
            taskRepository.save(task);
            taskEmailDecider(task, user, unchangedAssignedTo);
        } catch (Exception e) {
            log.error("Couldn't edit the task with id: {}", task.getId(), e);
            throw new RuntimeException("Something went wrong, editing the task with id: " + task.getId());
        }
    }

    private void taskEmailDecider(Task task, User user, boolean unchangedAssignedTo) {
        try {
            String title;
            if (unchangedAssignedTo) {
                title = "Task Update:";
                emailService.sendTaskDetailsEmail(user.getEmail(), task, title);
            } else {
                title = "You have been assigned a new task";
                emailService.sendTaskDetailsEmail(user.getEmail(), task, title);
            }
        } catch (Exception e) {
            log.error("Failed attempt to send task update to: {}", user.getUsername(), e);
        }
    }

    @Override
    public Page<Task> searchTasks(SearchReq searchReq, int page) {
        int actualPage = page - 1;
        String username = searchReq.getAssignedTo();
        String subject = searchReq.getSubject();
        Status status = searchReq.getStatus();
        LocalDate dueDate = searchReq.getDueDate();
        Pageable pageable = PageRequest.of(actualPage, 4, Sort.by(Sort.Direction.DESC, "dueDate"));

        Page<Task> tasks = taskRepository.findTasksBySearchReq(username, subject, status, dueDate, pageable);
        long totalCount = tasks.getTotalElements();

        return new PageImpl<>(tasks.getContent(), pageable, totalCount);
    }
}
