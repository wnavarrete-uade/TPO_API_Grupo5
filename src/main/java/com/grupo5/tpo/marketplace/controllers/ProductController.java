package com.grupo5.tpo.marketplace.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.grupo5.tpo.marketplace.model.Product;
import com.grupo5.tpo.marketplace.service.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // GET /products/{id} - Obtener producto por ID
    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable Long id) {
        return productService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET /products?categoryId={id} - Filtrar productos por categoría
    @GetMapping
    public ResponseEntity<List<Product>> getByCategoryId(@RequestParam Long categoryId) {
        return ResponseEntity.ok(productService.getByCategoryId(categoryId));
    }
}
