package com.grupo5.tpo.marketplace.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.grupo5.tpo.marketplace.exception.BadRequestException;
import com.grupo5.tpo.marketplace.exception.ResourceNotFoundException;
import com.grupo5.tpo.marketplace.model.Cart;
import com.grupo5.tpo.marketplace.model.CartItem;
import com.grupo5.tpo.marketplace.model.Product;
import com.grupo5.tpo.marketplace.model.User;
import com.grupo5.tpo.marketplace.repository.CartRepository;
import com.grupo5.tpo.marketplace.repository.ProductRepository;
import com.grupo5.tpo.marketplace.repository.UserRepository;

import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    public Cart getOrCreateCart(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        return cartRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    cart.setUser(user);
                    return cartRepository.save(cart);
                });
    }

    @Transactional
    public Cart addItem(String username, Long productId, Integer quantity) {
        Cart cart = getOrCreateCart(username);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));

        if (product.getStock() < quantity) {
            throw new BadRequestException("Stock insuficiente para: " + product.getName());
        }

        Optional<CartItem> existing = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        if (existing.isPresent()) {
            existing.get().setQuantity(existing.get().getQuantity() + quantity);
        } else {
            CartItem item = new CartItem();
            item.setCart(cart);
            item.setProduct(product);
            item.setQuantity(quantity);
            cart.getItems().add(item);
        }

        return cartRepository.save(cart);
    }

    @Transactional
    public Cart updateItemQuantity(String username, Long itemId, Integer quantity) {
        Cart cart = getOrCreateCart(username);

        CartItem item = cart.getItems().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Item no encontrado en el carrito"));

        if (quantity <= 0) {
            cart.getItems().remove(item);
        } else {
            item.setQuantity(quantity);
        }

        return cartRepository.save(cart);
    }

    @Transactional
    public Cart removeItem(String username, Long itemId) {
        Cart cart = getOrCreateCart(username);
        cart.getItems().removeIf(item -> item.getId().equals(itemId));
        return cartRepository.save(cart);
    }

    @Transactional
    public void clearCart(String username) {
        Cart cart = getOrCreateCart(username);
        cart.getItems().clear();
        cartRepository.save(cart);
    }
}
