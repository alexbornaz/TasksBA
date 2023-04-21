package com.tasksBA.tasksBAservice.service.user;

import com.tasksBA.tasksBAservice.model.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserService {
    Optional<User> getUserByUsername(String username);
}
