package com.grupo5.tpo.marketplace.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.grupo5.tpo.marketplace.model.Category;
import com.grupo5.tpo.marketplace.model.Product;
import com.grupo5.tpo.marketplace.model.User;
import com.grupo5.tpo.marketplace.repository.CategoryRepository;
import com.grupo5.tpo.marketplace.repository.ProductRepository;
import com.grupo5.tpo.marketplace.repository.UserRepository;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository,
                          UserRepository userRepository,
                          CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<Product> getAll() {
        return productRepository.findAll();
    }

    public Product create(Product product) {

        if (product.getSeller() != null) {
            User user = userRepository.findById(product.getSeller().getId())
                    .orElseThrow(() -> new RuntimeException("User no encontrado"));
            product.setSeller(user);
        }

        if (product.getCategory() != null) {
            Category category = categoryRepository.findById(product.getCategory().getId())
                    .orElseThrow(() -> new RuntimeException("Category no encontrada"));
            product.setCategory(category);
        }

        return productRepository.save(product);
    }
}