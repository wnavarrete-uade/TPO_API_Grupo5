package com.grupo5.tpo.marketplace.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.grupo5.tpo.marketplace.exception.BadRequestException;
import com.grupo5.tpo.marketplace.exception.ResourceNotFoundException;
import com.grupo5.tpo.marketplace.model.*;
import com.grupo5.tpo.marketplace.repository.OrderRepository;
import com.grupo5.tpo.marketplace.repository.ProductRepository;
import com.grupo5.tpo.marketplace.repository.UserRepository;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartService cartService;

    @Transactional
    public Order checkout(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        Cart cart = cartService.getOrCreateCart(username);

        if (cart.getItems().isEmpty()) {
            throw new BadRequestException("El carrito está vacío");
        }

        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.CONFIRMED);

        double total = 0;

        for (CartItem cartItem : cart.getItems()) {
            Product product = cartItem.getProduct();

            if (product.getStock() < cartItem.getQuantity()) {
                throw new BadRequestException("Stock insuficiente para: " + product.getName());
            }

            double finalPrice = product.getPrice() * (1 - product.getDiscount() / 100);

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setUnitPrice(finalPrice);
            order.getItems().add(orderItem);

            total += finalPrice * cartItem.getQuantity();

            product.setStock(product.getStock() - cartItem.getQuantity());
            productRepository.save(product);
        }

        order.setTotal(total);
        order = orderRepository.save(order);

        cartService.clearCart(username);

        return order;
    }

    public List<Order> getByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        return orderRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
    }

    public Order getById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada"));
    }
}
