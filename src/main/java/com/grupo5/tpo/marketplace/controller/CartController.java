package com.grupo5.tpo.marketplace.controller;

import java.security.Principal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.grupo5.tpo.marketplace.dto.CartItemRequest;
import com.grupo5.tpo.marketplace.model.Cart;
import com.grupo5.tpo.marketplace.service.CartService;

import jakarta.validation.Valid;

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
    public ResponseEntity<Map<String, Object>> addItem(@Valid @RequestBody CartItemRequest request, Principal principal) {
        Cart cart = cartService.addItem(principal.getName(), request.getProductId(), request.getQuantity());
        return ResponseEntity.status(201).body(Map.of(
                "message", "Producto agregado al carrito",
                "data", cart
        ));
    }

    @PutMapping("/items/{itemId}")
    public ResponseEntity<Map<String, Object>> updateItem(@PathVariable Long itemId, @RequestBody Map<String, Integer> body, Principal principal) {
        Cart cart = cartService.updateItemQuantity(principal.getName(), itemId, body.get("quantity"));
        return ResponseEntity.ok(Map.of(
                "message", "Cantidad actualizada correctamente",
                "data", cart
        ));
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<Map<String, Object>> removeItem(@PathVariable Long itemId, Principal principal) {
        Cart cart = cartService.removeItem(principal.getName(), itemId);
        return ResponseEntity.ok(Map.of(
                "message", "Producto eliminado del carrito",
                "data", cart
        ));
    }
}