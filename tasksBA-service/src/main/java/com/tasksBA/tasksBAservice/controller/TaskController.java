package com.tasksBA.tasksBAservice.controller;

import com.tasksBA.tasksBAservice.dto.TaskDTO;
import com.tasksBA.tasksBAservice.model.Task;
import com.tasksBA.tasksBAservice.service.task.TaskService;
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
    public ResponseEntity<Task> getTask(@PathVariable Long taskId) {
        Task task = taskService.getTask(taskId).get();
        return ResponseEntity.ok().body(task);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addTask(@RequestBody TaskDTO taskDTO) {
        try {
            taskService.createTask(taskDTO);
            return ResponseEntity.ok().body("Task added with success");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok().body("Couldn't add task");
        }
    }
}
