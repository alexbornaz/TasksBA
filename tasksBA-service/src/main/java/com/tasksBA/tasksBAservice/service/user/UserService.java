package com.tasksBA.tasksBAservice.service.user;

import com.tasksBA.tasksBAservice.model.User;

import java.util.List;
import java.util.Optional;


public interface UserService {
    List<User> getUsers();
    Optional<User> getUserByUsername(String username);

    boolean hasUserWithUsername(String username);
    boolean hasUserWithEmail(String email);

    User saveUser(User user);

    void deleteUser(User user);


}
