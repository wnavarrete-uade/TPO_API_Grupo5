package com.grupo5.tpo.marketplace.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.grupo5.tpo.marketplace.model.User;
import com.grupo5.tpo.marketplace.service.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        User created = userService.register(user);
        return ResponseEntity.status(201).body(created);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        return userService.login(username, password)
                .map(user -> ResponseEntity.ok(Map.of(
                        "message", "Login exitoso",
                        "userId", user.getId(),
                        "username", user.getUsername(),
                        "role", user.getRole()
                )))
                .orElse(ResponseEntity.status(401).body(Map.of("error", "Credenciales inválidas")));
    }
}
