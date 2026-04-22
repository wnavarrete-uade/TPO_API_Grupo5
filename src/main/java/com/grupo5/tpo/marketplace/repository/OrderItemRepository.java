package com.grupo5.tpo.marketplace.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.grupo5.tpo.marketplace.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
