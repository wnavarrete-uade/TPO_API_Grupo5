package com.grupo5.tpo.marketplace.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.grupo5.tpo.marketplace.model.Category;

@Service
public class CategoryService {

    private final List<Category> categories = new ArrayList<>(List.of(
            new Category(1L, "Electrónica", "Dispositivos electrónicos"),
            new Category(2L, "Ropa", "Indumentaria y accesorios"),
            new Category(3L, "Hogar", "Artículos para el hogar")
    ));

    public List<Category> getAll() {
        return categories;
    }
}
