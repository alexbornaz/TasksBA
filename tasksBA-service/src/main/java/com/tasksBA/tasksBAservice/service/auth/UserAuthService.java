package com.tasksBA.tasksBAservice.service.auth;

import com.tasksBA.tasksBAservice.dto.requests.LoginReq;
import com.tasksBA.tasksBAservice.dto.requests.SignUpReq;
import com.tasksBA.tasksBAservice.exceptions.UserNotFoundException;
import com.tasksBA.tasksBAservice.exceptions.auth.AuthenticationException;
import com.tasksBA.tasksBAservice.exceptions.auth.EmailAlreadyExistsException;
import com.tasksBA.tasksBAservice.exceptions.auth.UsernameAlreadyExistsException;
import com.tasksBA.tasksBAservice.exceptions.auth.UsernameOrPasswordExistsException;
import com.tasksBA.tasksBAservice.model.Role;
import com.tasksBA.tasksBAservice.model.User;
import com.tasksBA.tasksBAservice.service.EmailService;
import com.tasksBA.tasksBAservice.service.user.UserService;
import com.tasksBA.tasksBAservice.validator.ObjectValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class UserAuthService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final ObjectValidator<LoginReq> loginValidator;

    private final ObjectValidator<SignUpReq> signUpValidator;

    private final EmailService emailService;

    public UserAuthService(UserService userService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, TokenService tokenService, ObjectValidator<LoginReq> loginValidator, ObjectValidator<SignUpReq> signUpValidator, EmailService emailService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.loginValidator = loginValidator;
        this.signUpValidator = signUpValidator;
        this.emailService = emailService;
    }

    public User mapSignUpRequestToUser(SignUpReq signUpRequest) {
        String encodedPass = passwordEncoder.encode(signUpRequest.getPassword());
        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setRole(Role.USER);
        user.setPassword(encodedPass);
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
            log.error("Could not save user with username {}", signUpReq.getUsername(), e);
            throw new RuntimeException("Could not save the user in db");
        }
    }


    public String registerUser(SignUpReq signUpRequest) throws AuthenticationException {
        signUpValidator.validate(signUpRequest);
        if (userService.hasUserWithUsername(signUpRequest.getUsername())) {
            log.error("{}, username already exists, registration failed", signUpRequest.getUsername());
            throw new AuthenticationException(new UsernameAlreadyExistsException("Username already exists"));
        }
        if (userService.hasUserWithEmail(signUpRequest.getEmail())) {
            log.warn("{}, email already exists, registration failed", signUpRequest.getEmail());
            throw new AuthenticationException(new EmailAlreadyExistsException("Email already exists"));
        }
        addUser(signUpRequest);
        emailService.sendRegistrationMail(signUpRequest.getEmail(), signUpRequest.getUsername());
        return authenticate(signUpRequest.getUsername(), signUpRequest.getPassword());
    }

    public String login(LoginReq loginReq) throws AuthenticationException, UserNotFoundException {
        loginValidator.validate(loginReq);
        if (areValidCredentials(loginReq)) {
            return authenticate(loginReq.getUsername(), loginReq.getPassword());
        }
        throw new AuthenticationException(new UsernameOrPasswordExistsException("Username or password wrong!"));
    }

    public boolean areValidCredentials(LoginReq loginReq) throws UserNotFoundException {
        if (userService.hasUserWithUsername(loginReq.getUsername())) {
            Optional<User> user = userService.getUserByUsername(loginReq.getUsername());
            if (user.isPresent()) {
                String encodedPassword = user.get().getPassword();
                return passwordEncoder.matches(loginReq.getPassword(), encodedPassword);
            } else {
                throw new UserNotFoundException("User does not exist");
            }
        } else {
            log.warn("Login attempt from {} resulted unsuccessful", loginReq.getUsername());
            throw new UserNotFoundException("User does not exist");
        }
    }


}