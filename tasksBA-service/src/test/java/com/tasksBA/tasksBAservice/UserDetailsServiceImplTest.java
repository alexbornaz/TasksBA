package com.tasksBA.tasksBAservice;

import com.tasksBA.tasksBAservice.model.User;
import com.tasksBA.tasksBAservice.service.auth.UserDetailsServiceImpl;
import com.tasksBA.tasksBAservice.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplTest {
    @Mock
    UserService userService;

    @InjectMocks
    UserDetailsServiceImpl userDetailsService;
    private String username;

    @BeforeEach
    public void init() {
        username = "testuser";
    }

    @Test
    public void shouldLoadByUsername_ReturnUser() {
        User expectedUser = new User();
        expectedUser.setUsername(username);
        when(userService.getUserByUsername(username)).thenReturn(Optional.of(expectedUser));

        userDetailsService.loadUserByUsername(username);

        assertEquals(username, expectedUser.getUsername());
        verify(userService).getUserByUsername(username);
    }

    @Test
    public void shouldThrowError_WhenLoadByUsername() {
        when(userService.getUserByUsername(username)).thenThrow(new UsernameNotFoundException("error"));

        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(username));
        verify(userService).getUserByUsername(username);
    }
}
