package com.grupo5.tpo.marketplace.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.grupo5.tpo.marketplace.model.User;
import com.grupo5.tpo.marketplace.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User create(User user) {
        return userRepository.save(user);
    }
}