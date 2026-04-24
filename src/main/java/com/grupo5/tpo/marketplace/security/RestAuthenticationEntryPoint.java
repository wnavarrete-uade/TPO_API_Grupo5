package com.grupo5.tpo.marketplace.security;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Map<String, Object> body = Map.of(
                "timestamp", LocalDateTime.now().toString(),
                "status", 401,
                "error", "Unauthorized",
                "message", "Token ausente o inválido. Debe autenticarse para acceder a este recurso.",
                "path", request.getRequestURI()
        );

        mapper.writeValue(response.getOutputStream(), body);
    }
}