package com.grupo5.tpo.marketplace.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.grupo5.tpo.marketplace.model.Cart;
import com.grupo5.tpo.marketplace.service.CartService;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    // Por ahora recibimos userId como query param. Con JWT lo sacamos del token.
    @GetMapping
    public Cart getCart(@RequestParam Long userId) {
        return cartService.getOrCreateCart(userId);
    }

    @PostMapping("/items")
    public ResponseEntity<Cart> addItem(@RequestBody Map<String, Long> body) {
        Long userId = body.get("userId");
        Long productId = body.get("productId");
        Integer quantity = body.get("quantity") != null ? body.get("quantity").intValue() : 1;
        Cart cart = cartService.addItem(userId, productId, quantity);
        return ResponseEntity.status(201).body(cart);
    }

    @PutMapping("/items/{itemId}")
    public Cart updateItem(
            @PathVariable Long itemId,
            @RequestParam Long userId,
            @RequestBody Map<String, Integer> body) {
        return cartService.updateItemQuantity(userId, itemId, body.get("quantity"));
    }

    @DeleteMapping("/items/{itemId}")
    public Cart removeItem(@PathVariable Long itemId, @RequestParam Long userId) {
        return cartService.removeItem(userId, itemId);
    }
}
