package com.grupo5.tpo.marketplace.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.grupo5.tpo.marketplace.model.Product;

@Service
public class ProductService {

    private final AtomicLong idCounter = new AtomicLong(11);

    private final List<Product> products = new ArrayList<>(List.of(
            new Product(1L, "Whey Protein Gold Standard", "Proteína whey isolate 2lb - Sabor chocolate", 45000.0, 0.0, 50, "https://placeholder.com/whey-gold.jpg", 1L, 1L),
            new Product(2L, "Creatina Monohidrato 300g", "Creatina micronizada pura sin sabor", 22000.0, 10.0, 80, "https://placeholder.com/creatina.jpg", 2L, 1L),
            new Product(3L, "Pre-Workout C4 Original", "Pre-entreno con beta-alanina y cafeína - Sabor frutal", 38000.0, 0.0, 30, "https://placeholder.com/c4.jpg", 3L, 1L),
            new Product(4L, "BCAA 2:1:1 en polvo", "Aminoácidos ramificados 400g - Sabor uva", 18000.0, 0.0, 60, "https://placeholder.com/bcaa.jpg", 2L, 1L),
            new Product(5L, "Multivitamínico Daily Formula", "Complejo vitamínico y mineral - 100 tabletas", 15000.0, 5.0, 100, "https://placeholder.com/multi.jpg", 4L, 1L),
            new Product(6L, "Guantes de Gimnasio Pro", "Guantes con muñequera ajustable - Talle M", 12000.0, 0.0, 40, "https://placeholder.com/guantes.jpg", 5L, 1L),
            new Product(7L, "Faja Lumbar Neoprene", "Faja de soporte lumbar para levantamiento", 16000.0, 15.0, 25, "https://placeholder.com/faja.jpg", 5L, 1L),
            new Product(8L, "Remera Dry-Fit Entrenamiento", "Remera transpirable secado rápido - Talle L", 9500.0, 0.0, 70, "https://placeholder.com/remera.jpg", 6L, 1L),
            new Product(9L, "Proteína Vegana Orgánica", "Proteína plant-based 1kg - Sabor vainilla", 42000.0, 0.0, 20, "https://placeholder.com/vegana.jpg", 1L, 1L),
            new Product(10L, "Straps de Levantamiento", "Straps de algodón reforzado para peso muerto", 7500.0, 0.0, 55, "https://placeholder.com/straps.jpg", 5L, 1L)
    ));

    public List<Product> getAll() {
        return products;
    }

    public Optional<Product> getById(Long id) {
        return products.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }

    public List<Product> getByCategoryId(Long categoryId) {
        return products.stream()
                .filter(p -> p.getCategoryId().equals(categoryId))
                .collect(Collectors.toList());
    }

    public List<Product> search(String query) {
        String lowerQuery = query.toLowerCase();
        return products.stream()
                .filter(p -> p.getName().toLowerCase().contains(lowerQuery)
                        || p.getDescription().toLowerCase().contains(lowerQuery))
                .collect(Collectors.toList());
    }

    public Product create(Product product) {
        product.setId(idCounter.getAndIncrement());
        if (product.getDiscount() == null) product.setDiscount(0.0);
        products.add(product);
        return product;
    }

    public Optional<Product> update(Long id, Product updated) {
        return getById(id).map(existing -> {
            existing.setName(updated.getName());
            existing.setDescription(updated.getDescription());
            existing.setPrice(updated.getPrice());
            existing.setImageUrl(updated.getImageUrl());
            existing.setCategoryId(updated.getCategoryId());
            return existing;
        });
    }

    public boolean delete(Long id) {
        return products.removeIf(p -> p.getId().equals(id));
    }

    public Optional<Product> updateStock(Long id, Integer stock) {
        return getById(id).map(p -> {
            p.setStock(stock);
            return p;
        });
    }

    public Optional<Product> updateDiscount(Long id, Double discount) {
        return getById(id).map(p -> {
            p.setDiscount(discount);
            return p;
        });
    }
}
