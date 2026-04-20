package com.grupo5.tpo.marketplace.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.grupo5.tpo.marketplace.model.Category;
import com.grupo5.tpo.marketplace.repository.CategoryRepository;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    public Category create(Category category) {
        return categoryRepository.save(category);
    }
}