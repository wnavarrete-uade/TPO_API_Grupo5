package com.grupo5.tpo.marketplace.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Double discount; // porcentaje de descuento (0 a 100)
    private Integer stock;
    private String imageUrl;
    private Long categoryId;
    private Long sellerId;
}
