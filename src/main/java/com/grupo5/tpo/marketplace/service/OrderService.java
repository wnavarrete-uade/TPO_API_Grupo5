package com.grupo5.tpo.marketplace.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.grupo5.tpo.marketplace.model.Cart;
import com.grupo5.tpo.marketplace.model.Order;
import com.grupo5.tpo.marketplace.model.OrderItem;
import com.grupo5.tpo.marketplace.model.Product;

@Service
public class OrderService {

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    private final List<Order> orders = new ArrayList<>();
    private final AtomicLong orderIdCounter = new AtomicLong(1);
    private final AtomicLong itemIdCounter = new AtomicLong(1);

    public Order checkout(Long userId) {
        Cart cart = cartService.getOrCreateCart(userId);

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("El carrito está vacío");
        }

        // Crear order items y calcular total
        List<OrderItem> orderItems = new ArrayList<>();
        double total = 0;

        for (var cartItem : cart.getItems()) {
            Product product = productService.getById(cartItem.getProductId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            if (product.getStock() < cartItem.getQuantity()) {
                throw new RuntimeException("Stock insuficiente para: " + product.getName());
            }

            double finalPrice = product.getPrice() * (1 - product.getDiscount() / 100);

            OrderItem orderItem = new OrderItem();
            orderItem.setId(itemIdCounter.getAndIncrement());
            orderItem.setProductId(product.getId());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setUnitPrice(finalPrice);
            orderItems.add(orderItem);

            total += finalPrice * cartItem.getQuantity();

            // Descontar stock
            productService.updateStock(product.getId(), product.getStock() - cartItem.getQuantity());
        }

        // Crear la orden
        Order order = new Order();
        order.setId(orderIdCounter.getAndIncrement());
        order.setUserId(userId);
        order.setTotal(total);
        order.setStatus("CONFIRMED");
        order.setCreatedAt(LocalDateTime.now());
        order.setItems(orderItems);

        // Setear orderId en cada item
        orderItems.forEach(item -> item.setOrderId(order.getId()));

        orders.add(order);

        // Vaciar el carrito
        cartService.clearCart(userId);

        return order;
    }

    public List<Order> getByUserId(Long userId) {
        return orders.stream()
                .filter(o -> o.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    public Optional<Order> getById(Long id) {
        return orders.stream()
                .filter(o -> o.getId().equals(id))
                .findFirst();
    }
}
