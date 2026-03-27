package com.grupo5.tpo.marketplace.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import com.grupo5.tpo.marketplace.model.User;

@Service
public class UserService {

    private final AtomicLong idCounter = new AtomicLong(3);

    private final List<User> users = new ArrayList<>(List.of(
            new User(1L, "vendedor1", "vendedor@fitmarket.com", "123456", "Carlos", "García", "SELLER"),
            new User(2L, "comprador1", "comprador@fitmarket.com", "123456", "María", "López", "BUYER")
    ));

    public User register(User user) {
        user.setId(idCounter.getAndIncrement());
        users.add(user);
        return user;
    }

    public Optional<User> login(String username, String password) {
        return users.stream()
                .filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password))
                .findFirst();
    }

    public Optional<User> getById(Long id) {
        return users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst();
    }

    public Optional<User> update(Long id, User updated) {
        return getById(id).map(existing -> {
            existing.setFirstName(updated.getFirstName());
            existing.setLastName(updated.getLastName());
            existing.setEmail(updated.getEmail());
            return existing;
        });
    }
}
