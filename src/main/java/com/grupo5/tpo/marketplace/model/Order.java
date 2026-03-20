package com.grupo5.tpo.marketplace.model;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private Long id;
    private Long userId;
    private List<Long> productIds;
    private Double total;
    private LocalDateTime createdAt;
}
