package com.tasksBA.tasksBAservice;

import com.tasksBA.tasksBAservice.model.User;
import com.tasksBA.tasksBAservice.repository.UserRepository;
import com.tasksBA.tasksBAservice.service.user.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {
    private static final String username = "username";
    private static final String email = "email.example@email.com";

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

//    @BeforeEach
//    public void init() {
//    }

    @Test
    public void getUsersTest() {
        List<User> expectedListOfUsers = Arrays.asList(new User(), new User(), new User());
        when(userRepository.findAll()).thenReturn(expectedListOfUsers);

        List<User> actualUsersList = userService.getUsers();

        assertEquals(expectedListOfUsers, actualUsersList);
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void getUserByUsername_UserExists() {
        User user = new User();
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        Optional<User> actualUser = userService.getUserByUsername(username);

        assertTrue(actualUser.isPresent());
        assertEquals(user, actualUser.get());
        verify(userRepository).findByUsername(username);

    }

    @Test
    public void getUserByUsername_UserDoesNotExist() {
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        Optional<User> actualUser = userService.getUserByUsername(username);

        assertFalse(actualUser.isPresent());
        verify(userRepository).findByUsername(username);
    }

    @Test
    public void checkIfHasUserWithUsername_UserExists() {
        when(userRepository.existsByUsername(username)).thenReturn(true);

        boolean result = userService.hasUserWithUsername(username);

        assertTrue(result);
        verify(userRepository).existsByUsername(username);
    }

    @Test
    public void checkHasUserWithUsername_UserDoesNotExist() {
        when(userRepository.existsByUsername(username)).thenReturn(false);

        boolean result = userService.hasUserWithUsername(username);

        assertFalse(result);
        verify(userRepository).existsByUsername(username);
    }

    @Test
    public void checkHasUserWithEmail_UserExists() {
        when(userRepository.existsByEmail(email)).thenReturn(true);

        boolean result = userService.hasUserWithEmail(email);

        assertTrue(result);
        verify(userRepository).existsByEmail(email);
    }

    @Test
    public void testHasUserWithEmail_WhenUserDoesNotExist() {
        when(userRepository.existsByEmail(email)).thenReturn(false);

        boolean result = userService.hasUserWithEmail(email);

        assertFalse(result);
        verify(userRepository).existsByEmail(email);
    }
}
