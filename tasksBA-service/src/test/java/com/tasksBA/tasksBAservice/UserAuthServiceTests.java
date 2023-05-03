package com.tasksBA.tasksBAservice;

import com.tasksBA.tasksBAservice.dto.requests.LoginReq;
import com.tasksBA.tasksBAservice.dto.requests.SignUpReq;
import com.tasksBA.tasksBAservice.exceptions.UserNotFoundException;
import com.tasksBA.tasksBAservice.exceptions.auth.AuthenticationException;
import com.tasksBA.tasksBAservice.model.Role;
import com.tasksBA.tasksBAservice.model.User;
import com.tasksBA.tasksBAservice.service.auth.TokenService;
import com.tasksBA.tasksBAservice.service.auth.UserAuthService;
import com.tasksBA.tasksBAservice.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class UserAuthServiceTests {
    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private UserAuthService userAuthService;

    private SignUpReq signUpReq;
    private LoginReq loginReq;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        userAuthService = new UserAuthService(userService, passwordEncoder, authenticationManager, tokenService);
        signUpReq = new SignUpReq("testusername", "test.email@email.com", "123456");
        loginReq = new LoginReq("testusername", "123456");
    }

    @Test
    public void shouldCreateNewUserAndReturnAuthToken() throws AuthenticationException {
        User newUser = new User();
        newUser.setUsername(signUpReq.getUsername());
        newUser.setEmail(signUpReq.getEmail());
        newUser.setRole(Role.USER);
        newUser.setPassword(passwordEncoder.encode(signUpReq.getPassword()));
        when(userService.hasUserWithUsername(signUpReq.getUsername())).thenReturn(false);
        when(userService.hasUserWithEmail(signUpReq.getEmail())).thenReturn(false);
        when(userService.saveUser(newUser)).thenReturn(newUser);
        when(userAuthService.authenticate(signUpReq.getUsername(), signUpReq.getPassword())).thenReturn("token");
        String token = userAuthService.registerUser(signUpReq);
        assertEquals("token", token);
        verify(userService).hasUserWithUsername(signUpReq.getUsername());
        verify(userService).hasUserWithEmail(signUpReq.getEmail());
    }

    @Test
    public void registerUser_whenUsernameAlreadyExistsTest() {
        when(userService.hasUserWithUsername(signUpReq.getUsername())).thenReturn(true);
        assertThrows(AuthenticationException.class, () -> userAuthService.registerUser(signUpReq));
        Throwable exception = assertThrows(AuthenticationException.class, () -> userAuthService.registerUser(signUpReq));
        verify(userService, never()).saveUser(any());
        verify(authenticationManager, never()).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenService, never()).generate(any(Authentication.class));
    }

    @Test
    public void registerUser_whenEmailAlreadyExistsTest() {
        when(userService.hasUserWithUsername(signUpReq.getUsername())).thenReturn(false);
        when(userService.hasUserWithEmail(signUpReq.getEmail())).thenReturn(true);
        assertThrows(AuthenticationException.class, () -> userAuthService.registerUser(signUpReq));
        verify(userService, never()).saveUser(any());
        verify(authenticationManager, never()).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenService, never()).generate(any(Authentication.class));
    }

    @Test
    public void mappingSignUpRequestToUser_test() {
        when(passwordEncoder.encode(signUpReq.getPassword())).thenAnswer(invocationOnMock -> {
            String arg = invocationOnMock.getArgument(0);
            return arg != null ? arg.toUpperCase() : null;
        });
        User result = userAuthService.mapSignUpRequestToUser(signUpReq);
        when(passwordEncoder.matches(signUpReq.getPassword(), result.getPassword()))
                .thenReturn(signUpReq.getPassword().toUpperCase().equals(result.getPassword()));
        assertEquals(signUpReq.getUsername(), result.getUsername());
        assertEquals(signUpReq.getEmail(), result.getEmail());
        assertEquals(Role.USER, result.getRole());

        assertTrue(passwordEncoder.matches(signUpReq.getPassword(), result.getPassword()));
    }


    @Test
    public void shouldAddNewUserToDatabase() {
        when(passwordEncoder.encode(signUpReq.getPassword())).thenReturn("encodedPass");
        User expectedUser = new User();
        expectedUser.setUsername(signUpReq.getUsername());
        expectedUser.setPassword(signUpReq.getPassword());
        expectedUser.setRole(Role.USER);
        expectedUser.setEmail(signUpReq.getEmail());
        when(userAuthService.mapSignUpRequestToUser(signUpReq)).thenReturn(expectedUser);
        when(userService.saveUser(expectedUser)).thenReturn(expectedUser);
        userAuthService.addUser(signUpReq);
        verify(userService).saveUser(expectedUser);
    }

    @Test
    public void shouldAddNewUserToDatabase_ThrowError() {
        User expectedUser = new User(signUpReq.getUsername(), signUpReq.getEmail(), signUpReq.getPassword(), Role.USER);
        when(passwordEncoder.encode(anyString())).thenAnswer(invocationOnMock -> {
            String arg = invocationOnMock.getArgument(0);
            return arg != null ? arg.toUpperCase() : null;
        });
        when(userAuthService.mapSignUpRequestToUser(signUpReq)).thenReturn(expectedUser);
        when(userService.saveUser(expectedUser)).thenThrow(Exception.class);
        assertThrows(Exception.class, () -> userAuthService.addUser(signUpReq));
    }


    @Test
    void authenticate_ShouldReturnToken_WhenValidCredentialsProvided() {
        String testToken = "testToken";
        Authentication authentication = new UsernamePasswordAuthenticationToken(loginReq.getUsername(), loginReq.getPassword());
        when(authenticationManager.authenticate(authentication)).thenReturn(authentication);
        when(tokenService.generate(authentication)).thenReturn(testToken);
        String result = userAuthService.authenticate(loginReq.getUsername(), loginReq.getPassword());
        assertEquals(testToken, result);
        verify(authenticationManager).authenticate(authentication);
        verify(tokenService).generate(authentication);
    }

    @Test
    public void shouldLoginSuccessful() throws AuthenticationException, UserNotFoundException {
        User user = new User();
        when(passwordEncoder.matches(loginReq.getPassword(), user.getPassword())).thenReturn(true);
        when(userService.getUserByUsername(loginReq.getUsername())).thenReturn(Optional.of(user));
        when(userAuthService.areValidCredentials(loginReq)).thenReturn(true);
        when(userAuthService.authenticate(loginReq.getUsername(), loginReq.getPassword())).thenReturn("testtoken");
        String token = userAuthService.login(loginReq);
        assertEquals("testtoken", token);
    }
}

