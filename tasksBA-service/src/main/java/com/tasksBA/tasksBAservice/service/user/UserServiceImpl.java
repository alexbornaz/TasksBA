package com.tasksBA.tasksBAservice.service.user;

import com.tasksBA.tasksBAservice.dto.responses.UserDTO;
import com.tasksBA.tasksBAservice.exceptions.UserNotFoundException;
import com.tasksBA.tasksBAservice.model.User;
import com.tasksBA.tasksBAservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public boolean hasUserWithUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean hasUserWithEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public void deleteUserByUsername(String username) throws UserNotFoundException {
        User userToBeDeleted = getUserByUsername(username).orElseThrow(
                () -> new UserNotFoundException("Could not found user " + username + " to delete"));
        try {
            userRepository.delete(userToBeDeleted);
        } catch (Exception e) {
            log.error("Failed attempt to delete user {}", username, e);
            throw new RuntimeException("Could not delete user " + username + " from database");
        }
    }

    @Override
    public List<UserDTO> getUsernames() {
        return userRepository.getUsernames();
    }
}
