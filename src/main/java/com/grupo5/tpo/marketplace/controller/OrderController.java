package com.grupo5.tpo.marketplace.controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.grupo5.tpo.marketplace.model.Order;
import com.grupo5.tpo.marketplace.service.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/checkout")
    public ResponseEntity<Map<String, Object>> checkout(Principal principal) {
        Order order = orderService.checkout(principal.getName());
        return ResponseEntity.status(201).body(Map.of(
                "message", "Orden creada correctamente",
                "data", order
        ));
    }

    @GetMapping
    public List<Order> getMyOrders(Principal principal) {
        return orderService.getByUsername(principal.getName());
    }

    @GetMapping("/{id}")
    public Order getById(@PathVariable Long id) {
        return orderService.getById(id);
    }
}