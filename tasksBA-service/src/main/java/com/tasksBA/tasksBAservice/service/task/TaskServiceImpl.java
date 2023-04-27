package com.tasksBA.tasksBAservice.service.task;

import com.tasksBA.tasksBAservice.dto.SearchReq;
import com.tasksBA.tasksBAservice.dto.requests.TaskDTO;
import com.tasksBA.tasksBAservice.model.Status;
import com.tasksBA.tasksBAservice.model.Task;
import com.tasksBA.tasksBAservice.model.User;
import com.tasksBA.tasksBAservice.repository.TaskRepository;
import com.tasksBA.tasksBAservice.service.user.UserService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
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
    public Optional<Task> getTask(Long id) {
        return taskRepository.findById(id);
    }

    @Override
    public List<Task> getAssignedTasks(String username) {
        User user = userService.getUserByUsername(username).get();
        return taskRepository.findAllByAssignedToOrderByDueDateDesc(user);
    }

    @Override
    public void createTask(TaskDTO taskDTO) {
        User user = userService.getUserByUsername(taskDTO.getAssignedTo()).get();
        Task task = new Task(taskDTO.getSubject(), taskDTO.getDueDate(), user);
        task.setSubject(taskDTO.getSubject());
        task.setDueDate(taskDTO.getDueDate());
        task.setAssignedTo(user);
        task.setStatus(taskDTO.getStatus());
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

    @Override
    public List<Task> searchTasks(SearchReq searchReq) {
        String username = searchReq.getAssignedTo();
        User user;
        if (username != null) {
            user = userService.getUserByUsername(username).get();
        } else {
            user = null;
        }
        String subject = searchReq.getSubject();
        Status status = searchReq.getStatus();
        LocalDate dueDate = searchReq.getDueDate();
        Task task = new Task();
        task.setAssignedTo(user);
        task.setSubject(subject);
        task.setStatus(status);

        Example<Task> example = Example.of(task, ExampleMatcher.matchingAll()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
        );
        if (dueDate != null) {
            return taskRepository.findAll(example).stream().filter(task1 -> task1.getDueDate().compareTo(dueDate) > 0)
                    .collect(Collectors.toList());
        }
        return taskRepository.findAll(example);
    }
}
