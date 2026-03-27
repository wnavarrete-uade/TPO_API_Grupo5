package com.grupo5.tpo.marketplace.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import com.grupo5.tpo.marketplace.model.Cart;
import com.grupo5.tpo.marketplace.model.CartItem;

@Service
public class CartService {

    private final Map<Long, Cart> cartsByUserId = new HashMap<>();
    private final AtomicLong cartIdCounter = new AtomicLong(1);
    private final AtomicLong itemIdCounter = new AtomicLong(1);

    public Cart getOrCreateCart(Long userId) {
        return cartsByUserId.computeIfAbsent(userId, id -> {
            Cart cart = new Cart();
            cart.setId(cartIdCounter.getAndIncrement());
            cart.setUserId(id);
            cart.setCreatedAt(LocalDateTime.now());
            cart.setItems(new ArrayList<>());
            return cart;
        });
    }

    public Cart addItem(Long userId, Long productId, Integer quantity) {
        Cart cart = getOrCreateCart(userId);

        // Si el producto ya está en el carrito, sumar cantidad
        Optional<CartItem> existing = cart.getItems().stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst();

        if (existing.isPresent()) {
            existing.get().setQuantity(existing.get().getQuantity() + quantity);
        } else {
            CartItem item = new CartItem();
            item.setId(itemIdCounter.getAndIncrement());
            item.setCartId(cart.getId());
            item.setProductId(productId);
            item.setQuantity(quantity);
            cart.getItems().add(item);
        }

        return cart;
    }

    public Cart updateItemQuantity(Long userId, Long itemId, Integer quantity) {
        Cart cart = getOrCreateCart(userId);
        cart.getItems().stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .ifPresent(item -> item.setQuantity(quantity));
        return cart;
    }

    public Cart removeItem(Long userId, Long itemId) {
        Cart cart = getOrCreateCart(userId);
        cart.getItems().removeIf(item -> item.getId().equals(itemId));
        return cart;
    }

    public void clearCart(Long userId) {
        Cart cart = getOrCreateCart(userId);
        cart.getItems().clear();
    }
}
