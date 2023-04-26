package com.tasksBA.tasksBAservice.service.auth;

import com.tasksBA.tasksBAservice.dto.requests.LoginReq;
import com.tasksBA.tasksBAservice.dto.requests.SignUpReq;
import com.tasksBA.tasksBAservice.exceptions.auth.AuthenticationException;
import com.tasksBA.tasksBAservice.exceptions.auth.EmailAlreadyExistsException;
import com.tasksBA.tasksBAservice.exceptions.auth.UsernameAlreadyExistsException;
import com.tasksBA.tasksBAservice.exceptions.auth.UsernameOrPasswordExistsException;
import com.tasksBA.tasksBAservice.model.Role;
import com.tasksBA.tasksBAservice.model.User;
import com.tasksBA.tasksBAservice.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserAuthService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public UserAuthService(UserService userService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, TokenService tokenService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    public User mapSignUpRequestToUser(SignUpReq signUpRequest) {
        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        return user;
    }

    public String authenticate(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        return tokenService.generate(authentication);
    }

    public void addUser(SignUpReq signUpReq) {
        User newUser = mapSignUpRequestToUser(signUpReq);
        try {
            userService.saveUser(newUser);
        } catch (Exception e) {
            log.info("Could not save user with username {}", signUpReq.getUsername());
        }
    }


    public String registerUser(SignUpReq signUpRequest) throws AuthenticationException {
        if (userService.hasUserWithUsername(signUpRequest.getUsername())) {
            log.error("{}, username already exists, registration failed", signUpRequest.getUsername());
            throw new AuthenticationException(new UsernameAlreadyExistsException("Username already exists"));
        }
        if (userService.hasUserWithEmail(signUpRequest.getUsername())) {
            log.warn("{}, email already exists, registration failed", signUpRequest.getEmail());
            throw new AuthenticationException(new EmailAlreadyExistsException("Email already exists"));
        }
        addUser(signUpRequest);
        return authenticate(signUpRequest.getUsername(), signUpRequest.getPassword());
    }

    public String login(LoginReq loginReq) throws AuthenticationException {
        if (areValidCredentials(loginReq)) {
            return authenticate(loginReq.getUsername(), loginReq.getPassword());
        }
        throw new AuthenticationException(new UsernameOrPasswordExistsException("Username or password wrong!"));
    }

    private boolean areValidCredentials(LoginReq loginReq) {
        if (userService.hasUserWithUsername(loginReq.getUsername())) {
            String encodedPassword = userService.getUserByUsername(loginReq.getUsername()).get().getPassword();
            return passwordEncoder.matches(loginReq.getPassword(), encodedPassword);
        }
        log.warn("Login attempt from {} resulted unsuccessful", loginReq.getUsername());
        return false;
    }
}