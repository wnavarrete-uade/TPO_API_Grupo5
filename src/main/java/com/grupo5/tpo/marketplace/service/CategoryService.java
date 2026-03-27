package com.grupo5.tpo.marketplace.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.grupo5.tpo.marketplace.model.Category;

@Service
public class CategoryService {

    private final List<Category> categories = new ArrayList<>(List.of(
            new Category(1L, "Proteínas", "Whey, caseína, vegana y más"),
            new Category(2L, "Creatina y Aminoácidos", "Creatina monohidrato, BCAA, glutamina"),
            new Category(3L, "Pre-entrenos", "Suplementos pre-workout y energizantes"),
            new Category(4L, "Vitaminas", "Multivitamínicos y minerales"),
            new Category(5L, "Accesorios", "Guantes, fajas, straps, muñequeras"),
            new Category(6L, "Indumentaria", "Ropa deportiva para entrenamiento")
    ));

    public List<Category> getAll() {
        return categories;
    }

    public Optional<Category> getById(Long id) {
        return categories.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();
    }
}
