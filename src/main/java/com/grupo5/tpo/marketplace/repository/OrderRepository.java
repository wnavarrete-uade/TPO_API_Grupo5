package com.grupo5.tpo.marketplace.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.grupo5.tpo.marketplace.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserIdOrderByCreatedAtDesc(Long userId);
}
