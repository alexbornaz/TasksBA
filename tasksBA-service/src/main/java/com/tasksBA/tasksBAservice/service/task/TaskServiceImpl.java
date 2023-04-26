package com.tasksBA.tasksBAservice.service.task;

import com.tasksBA.tasksBAservice.dto.requests.TaskDTO;
import com.tasksBA.tasksBAservice.model.Task;
import com.tasksBA.tasksBAservice.model.User;
import com.tasksBA.tasksBAservice.repository.TaskRepository;
import com.tasksBA.tasksBAservice.service.user.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class TaskServiceImpl implements TaskService{
    private final TaskRepository taskRepository;
    private final UserService userService;

    public TaskServiceImpl(TaskRepository taskRepository, UserService userService) {
        this.taskRepository = taskRepository;
        this.userService = userService;
    }


    @Override
    public List<Task> getAll() {
        return taskRepository.findAll();
    }

    @Override
    public Optional<Task> getTask(Long id) {
        return taskRepository.findById(id);
    }

    @Override
    public void createTask(TaskDTO taskDTO) {
        User user = userService.getUserByUsername(taskDTO.getAssignedTo()).get();
        Task task = new Task(taskDTO.getSubject(),taskDTO.getDueDate(),user);
        task.setSubject(taskDTO.getSubject());
        task.setDueDate(taskDTO.getDueDate());
        task.setAssignedTo(user);
        taskRepository.save(task);
        user.addTaskToAssigned(task);
        userService.saveUser(user);
    }


    @Override
    public void deleteTask(Task task) {
        User user = userService.getUserByUsername(task.getAssignedTo().getUsername()).get();
        user.removeTaskFromAssigned(task);
        userService.saveUser(user);
        taskRepository.delete(task);
    }

    @Override
    public void editTask(TaskDTO taskDTO) {
        User user = userService.getUserByUsername(taskDTO.getAssignedTo()).get();
        Task task = taskRepository.findById(taskDTO.getId()).get();
        task.setSubject(taskDTO.getSubject());
        task.setDueDate(taskDTO.getDueDate());
        task.setStatus(taskDTO.getStatus());
        task.setAssignedTo(user);
        taskRepository.save(task);
    }
}
