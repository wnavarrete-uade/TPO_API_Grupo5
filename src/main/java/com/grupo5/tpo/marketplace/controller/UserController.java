package com.grupo5.tpo.marketplace.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.grupo5.tpo.marketplace.model.User;
import com.grupo5.tpo.marketplace.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/me")
    public User getProfile(Principal principal) {
        return userService.getByUsername(principal.getName());
    }

    @PutMapping("/me")
    public User updateProfile(@RequestBody User updated, Principal principal) {
        User current = userService.getByUsername(principal.getName());
        return userService.update(current.getId(), updated);
    }
}
