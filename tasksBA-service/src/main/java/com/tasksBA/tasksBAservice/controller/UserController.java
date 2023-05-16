package com.tasksBA.tasksBAservice.controller;

import com.tasksBA.tasksBAservice.dto.responses.UserDTO;
import com.tasksBA.tasksBAservice.exceptions.UserNotFoundException;
import com.tasksBA.tasksBAservice.model.Task;
import com.tasksBA.tasksBAservice.service.task.TaskService;
import com.tasksBA.tasksBAservice.service.user.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final TaskService taskService;

    public UserController(UserService userService, TaskService taskService) {
        this.userService = userService;
        this.taskService = taskService;
    }

    @GetMapping("/tasks/{username}")
    public ResponseEntity<?> getAssignedTasks(@PathVariable String username, @RequestParam(defaultValue = "1") int page) throws UserNotFoundException {
        Page<Task> tasks = taskService.getAssignedTasks(username, page);
        return ResponseEntity.ok().body(tasks);
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<String> deleteUser(@PathVariable String username) throws UserNotFoundException {
        userService.deleteUserByUsername(username);
        return ResponseEntity.ok().body("User deleted");
    }

    @GetMapping("/usernames")
    public ResponseEntity<List<UserDTO>> getListOfUsernames() {
        return ResponseEntity.ok().body(userService.getUsernames());
    }
}
