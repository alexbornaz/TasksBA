package com.tasksBA.tasksBAservice.controller;

import com.tasksBA.tasksBAservice.dto.requests.LoginReq;
import com.tasksBA.tasksBAservice.dto.requests.SignUpReq;
import com.tasksBA.tasksBAservice.exceptions.UserNotFoundException;
import com.tasksBA.tasksBAservice.exceptions.auth.AuthenticationException;
import com.tasksBA.tasksBAservice.service.auth.UserAuthService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    private final UserAuthService userAuthService;

    public AuthenticationController(UserAuthService userAuthService) {
        this.userAuthService = userAuthService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> signUp(@RequestBody SignUpReq signUpReq) throws AuthenticationException {
        String token = userAuthService.registerUser(signUpReq);
        return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, token).build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginReq loginReq) throws AuthenticationException, UserNotFoundException {
        String token = userAuthService.login(loginReq);
        return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, token).build();
    }
}
