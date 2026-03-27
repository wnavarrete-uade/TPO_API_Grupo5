package com.grupo5.tpo.marketplace.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
    private Long id;
    private Long orderId;
    private Long productId;
    private Integer quantity;
    private Double unitPrice;
}
