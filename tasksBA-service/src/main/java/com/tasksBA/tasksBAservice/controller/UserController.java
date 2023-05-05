package com.tasksBA.tasksBAservice.controller;

import com.tasksBA.tasksBAservice.dto.responses.UserDTO;
import com.tasksBA.tasksBAservice.exceptions.UserNotFoundException;
import com.tasksBA.tasksBAservice.model.Task;
import com.tasksBA.tasksBAservice.model.User;
import com.tasksBA.tasksBAservice.service.task.TaskService;
import com.tasksBA.tasksBAservice.service.user.UserService;
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
    public ResponseEntity<?> getAssignedTasks(@PathVariable String username) throws UserNotFoundException {
        List<Task> tasks = taskService.getAssignedTasks(username);
        return ResponseEntity.ok().body(tasks);
    }

    @DeleteMapping("/{usernane}")
    public ResponseEntity<String> deleteUser(@PathVariable String usernane) {
        User user = userService.getUserByUsername(usernane).get();
        try {
            userService.deleteUser(user);
            return ResponseEntity.ok().body("User deleted");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok().body("Couldn't delete user");
        }
    }

    @GetMapping("/usernames")
    public ResponseEntity<List<UserDTO>> getListOfUsernames() {
        return ResponseEntity.ok().body(userService.getUsernames());
    }
}
