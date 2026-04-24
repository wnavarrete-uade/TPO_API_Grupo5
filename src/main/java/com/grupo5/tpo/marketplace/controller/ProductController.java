package com.grupo5.tpo.marketplace.controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.grupo5.tpo.marketplace.dto.ProductRequest;
import com.grupo5.tpo.marketplace.model.Product;
import com.grupo5.tpo.marketplace.service.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public List<Product> getAll(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long sellerId,
            @RequestParam(required = false) String search) {

        if (categoryId != null) {
            return productService.getByCategoryId(categoryId);
        }
        if (sellerId != null) {
            return productService.getBySellerId(sellerId);
        }
        if (search != null && !search.isBlank()) {
            return productService.search(search);
        }
        return productService.getAll();
    }

    @GetMapping("/{id}")
    public Product getById(@PathVariable Long id) {
        return productService.getById(id);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@Valid @RequestBody ProductRequest request, Principal principal) {
        Product created = productService.create(request, principal.getName());
        return ResponseEntity.status(201).body(Map.of(
                "message", "Producto creado correctamente",
                "data", created
        ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @Valid @RequestBody ProductRequest request, Principal principal) {
        Product updated = productService.update(id, request, principal.getName());
        return ResponseEntity.ok(Map.of(
                "message", "Producto actualizado correctamente",
                "data", updated
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id, Principal principal) {
        productService.delete(id, principal.getName());
        return ResponseEntity.ok(Map.of("message", "Producto eliminado correctamente"));
    }

    @PatchMapping("/{id}/stock")
    public ResponseEntity<Map<String, Object>> updateStock(@PathVariable Long id, @RequestBody Map<String, Integer> body, Principal principal) {
        Product updated = productService.updateStock(id, body.get("stock"), principal.getName());
        return ResponseEntity.ok(Map.of(
                "message", "Stock actualizado correctamente",
                "data", updated
        ));
    }

    @PatchMapping("/{id}/discount")
    public ResponseEntity<Map<String, Object>> updateDiscount(@PathVariable Long id, @RequestBody Map<String, Double> body, Principal principal) {
        Product updated = productService.updateDiscount(id, body.get("discount"), principal.getName());
        return ResponseEntity.ok(Map.of(
                "message", "Descuento actualizado correctamente",
                "data", updated
        ));
    }
}