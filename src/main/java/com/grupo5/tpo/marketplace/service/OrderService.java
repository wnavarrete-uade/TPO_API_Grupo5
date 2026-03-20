package com.grupo5.tpo.marketplace.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import com.grupo5.tpo.marketplace.model.Order;

@Service
public class OrderService {

    private final List<Order> orders = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    public Order create(Order order) {
        order.setId(idCounter.getAndIncrement());
        order.setCreatedAt(LocalDateTime.now());
        orders.add(order);
        return order;
    }
}
