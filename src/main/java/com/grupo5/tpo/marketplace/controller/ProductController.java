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
    public ResponseEntity<Product> create(@Valid @RequestBody ProductRequest request, Principal principal) {
        Product created = productService.create(request, principal.getName());
        return ResponseEntity.status(201).body(created);
    }

    @PutMapping("/{id}")
    public Product update(@PathVariable Long id, @Valid @RequestBody ProductRequest request, Principal principal) {
        return productService.update(id, request, principal.getName());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, Principal principal) {
        productService.delete(id, principal.getName());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/stock")
    public Product updateStock(@PathVariable Long id, @RequestBody Map<String, Integer> body, Principal principal) {
        return productService.updateStock(id, body.get("stock"), principal.getName());
    }

    @PatchMapping("/{id}/discount")
    public Product updateDiscount(@PathVariable Long id, @RequestBody Map<String, Double> body, Principal principal) {
        return productService.updateDiscount(id, body.get("discount"), principal.getName());
    }
}
