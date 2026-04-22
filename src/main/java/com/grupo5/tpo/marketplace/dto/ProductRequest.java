package com.grupo5.tpo.marketplace.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductRequest {
    @NotBlank
    private String name;

    private String description;

    @NotNull
    @Min(0)
    private Double price;

    private Double discount = 0.0;

    @NotNull
    @Min(0)
    private Integer stock;

    private String imageUrl;

    @NotNull
    private Long categoryId;
}
