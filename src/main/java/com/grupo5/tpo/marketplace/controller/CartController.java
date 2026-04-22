package com.grupo5.tpo.marketplace.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.grupo5.tpo.marketplace.dto.CartItemRequest;
import com.grupo5.tpo.marketplace.model.Cart;
import com.grupo5.tpo.marketplace.service.CartService;

import jakarta.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping
    public Cart getCart(Principal principal) {
        return cartService.getOrCreateCart(principal.getName());
    }

    @PostMapping("/items")
    public ResponseEntity<Cart> addItem(@Valid @RequestBody CartItemRequest request, Principal principal) {
        Cart cart = cartService.addItem(principal.getName(), request.getProductId(), request.getQuantity());
        return ResponseEntity.status(201).body(cart);
    }

    @PutMapping("/items/{itemId}")
    public Cart updateItem(@PathVariable Long itemId, @RequestBody Map<String, Integer> body, Principal principal) {
        return cartService.updateItemQuantity(principal.getName(), itemId, body.get("quantity"));
    }

    @DeleteMapping("/items/{itemId}")
    public Cart removeItem(@PathVariable Long itemId, Principal principal) {
        return cartService.removeItem(principal.getName(), itemId);
    }
}
