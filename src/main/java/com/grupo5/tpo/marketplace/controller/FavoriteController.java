package com.grupo5.tpo.marketplace.controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.grupo5.tpo.marketplace.model.Product;
import com.grupo5.tpo.marketplace.service.FavoriteService;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    // GET /favorites - Lista los productos favoritos del usuario logueado
    @GetMapping
    public List<Product> getFavorites(Principal principal) {
        return favoriteService.getFavorites(principal.getName());
    }

    // POST /favorites/{productId} - Agrega un producto a favoritos
    @PostMapping("/{productId}")
    public ResponseEntity<Map<String, String>> addFavorite(Principal principal, @PathVariable Long productId) {
        favoriteService.addFavorite(principal.getName(), productId);
        return ResponseEntity.status(201).body(Map.of("message", "Producto agregado a favoritos"));
    }

    // DELETE /favorites/{productId} - Quita un producto de favoritos
    @DeleteMapping("/{productId}")
    public ResponseEntity<Map<String, String>> removeFavorite(Principal principal, @PathVariable Long productId) {
        favoriteService.removeFavorite(principal.getName(), productId);
        return ResponseEntity.ok(Map.of("message", "Producto eliminado de favoritos"));
    }

    // GET /favorites/{productId}/check - Verifica si un producto esta en favoritos
    @GetMapping("/{productId}/check")
    public Map<String, Boolean> isFavorite(Principal principal, @PathVariable Long productId) {
        return Map.of("isFavorite", favoriteService.isFavorite(principal.getName(), productId));
    }
}
