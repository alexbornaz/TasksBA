package com.tasksBA.tasksBAservice.service.auth;

import com.tasksBA.tasksBAservice.model.User;
import com.tasksBA.tasksBAservice.service.user.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserService userService;

    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userService.getUserByUsername(username)
                .orElseThrow(()->new UsernameNotFoundException(String.format("Username %s not found",username)));
    }
}
