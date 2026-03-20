package com.grupo5.tpo.marketplace.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import com.grupo5.tpo.marketplace.model.User;

@Service
public class UserService {

    private final List<User> users = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    public User create(User user) {
        user.setId(idCounter.getAndIncrement());
        users.add(user);
        return user;
    }
}
