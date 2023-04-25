package com.tasksBA.tasksBAservice.controller;

import com.tasksBA.tasksBAservice.model.Task;
import com.tasksBA.tasksBAservice.model.User;
import com.tasksBA.tasksBAservice.service.task.TaskService;
import com.tasksBA.tasksBAservice.service.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    public UserController(TaskService taskService, UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/tasks/{username}")
    public ResponseEntity<Set<Task>> getAssignedTasks(@PathVariable String username){
        Set<Task> tasks = userService.getUserByUsername(username).get().getAssignedTasks();
        return ResponseEntity.ok().body(tasks);
    }

    @DeleteMapping("/delete/{usernane}")
    public ResponseEntity<String> deleteUser(@PathVariable String usernane){
        User user = userService.getUserByUsername(usernane).get();
        try{
            userService.deleteUser(user);
            return ResponseEntity.ok().body("User deleted");
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.ok().body("Couldn't delete user");
        }
    }
}
