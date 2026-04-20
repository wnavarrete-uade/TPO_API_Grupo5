package com.grupo5.tpo.marketplace.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.grupo5.tpo.marketplace.model.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}