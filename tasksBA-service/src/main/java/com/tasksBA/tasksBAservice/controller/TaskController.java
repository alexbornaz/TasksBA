package com.tasksBA.tasksBAservice.controller;

import com.tasksBA.tasksBAservice.dto.requests.SearchReq;
import com.tasksBA.tasksBAservice.dto.requests.TaskDTO;
import com.tasksBA.tasksBAservice.dto.responses.MessageResp;
import com.tasksBA.tasksBAservice.exceptions.UserNotFoundException;
import com.tasksBA.tasksBAservice.model.Task;
import com.tasksBA.tasksBAservice.service.task.TaskService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<Task> getTask(@PathVariable @Valid Long taskId) {
        Task desiredTask = taskService.getTask(taskId);
        return ResponseEntity.ok().body(desiredTask);
    }

    @GetMapping()
    ResponseEntity<Page<Task>> getAllTasks(@RequestParam(defaultValue = "1") int page) {
        Page<Task> tasks = taskService.getAll(page);
        return ResponseEntity.ok().body(tasks);
    }

    @PostMapping("/add")
    public ResponseEntity<MessageResp> addTask(@RequestBody TaskDTO taskDTO) throws UserNotFoundException {
        taskService.createTask(taskDTO);
        return ResponseEntity.ok().body(new MessageResp("Task added with success"));
    }

    @PutMapping("/edit")
    public ResponseEntity<?> editTask(@RequestBody TaskDTO taskDTO) throws UserNotFoundException {
        taskService.editTask(taskDTO);
        return ResponseEntity.ok().body(new MessageResp("Task edited with success"));
    }

    @PostMapping("/search")
    public ResponseEntity<?> getSearchedTasks(@RequestBody SearchReq searchReq, @RequestParam int page) {
        Page<Task> tasks = taskService.searchTasks(searchReq, page);
        return ResponseEntity.ok().body(tasks);
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<?> deleteTask(@PathVariable @Valid Long taskId) throws UserNotFoundException {
        taskService.deleteTask(taskId);
        return ResponseEntity.ok().body(new MessageResp("Task deleted with success"));
    }
}
