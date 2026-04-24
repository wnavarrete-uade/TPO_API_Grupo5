package com.grupo5.tpo.marketplace.controller;

import java.security.Principal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Map<String, Object>> updateProfile(@RequestBody User updated, Principal principal) {
        User current = userService.getByUsername(principal.getName());
        User saved = userService.update(current.getId(), updated);
        return ResponseEntity.ok(Map.of(
                "message", "Perfil actualizado correctamente",
                "data", saved
        ));
    }
}