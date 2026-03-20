package com.grupo5.tpo.marketplace.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.grupo5.tpo.marketplace.model.Product;

@Service
public class ProductService {

    private final List<Product> products = new ArrayList<>(List.of(
            new Product(1L, "Notebook Lenovo", "Laptop 15 pulgadas", 850000.0, 1L),
            new Product(2L, "Remera Nike", "Remera deportiva talle M", 35000.0, 2L),
            new Product(3L, "Auriculares Sony", "Auriculares bluetooth", 120000.0, 1L),
            new Product(4L, "Silla Gamer", "Silla ergonómica", 250000.0, 3L)
    ));

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
}
