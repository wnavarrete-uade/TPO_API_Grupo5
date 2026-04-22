package com.grupo5.tpo.marketplace.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.grupo5.tpo.marketplace.dto.AuthResponse;
import com.grupo5.tpo.marketplace.dto.LoginRequest;
import com.grupo5.tpo.marketplace.dto.RegisterRequest;
import com.grupo5.tpo.marketplace.exception.BadRequestException;
import com.grupo5.tpo.marketplace.exception.ResourceNotFoundException;
import com.grupo5.tpo.marketplace.model.Role;
import com.grupo5.tpo.marketplace.model.User;
import com.grupo5.tpo.marketplace.repository.UserRepository;
import com.grupo5.tpo.marketplace.security.JwtUtil;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("El nombre de usuario ya existe");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("El email ya está registrado");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setRole(request.getRole() != null && request.getRole().equalsIgnoreCase("SELLER")
                ? Role.SELLER : Role.BUYER);

        user = userRepository.save(user);

        String token = jwtUtil.generateToken(user.getUsername(), user.getId(), user.getRole().name());

        return new AuthResponse(token, user.getId(), user.getUsername(),
                user.getFirstName(), user.getLastName(), user.getEmail(), user.getRole().name());
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BadRequestException("Credenciales inválidas"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadRequestException("Credenciales inválidas");
        }

        String token = jwtUtil.generateToken(user.getUsername(), user.getId(), user.getRole().name());

        return new AuthResponse(token, user.getId(), user.getUsername(),
                user.getFirstName(), user.getLastName(), user.getEmail(), user.getRole().name());
    }

    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
    }

    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
    }

    public User update(Long id, User updated) {
        User user = getById(id);
        user.setFirstName(updated.getFirstName());
        user.setLastName(updated.getLastName());
        user.setEmail(updated.getEmail());
        return userRepository.save(user);
    }
}
