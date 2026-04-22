package com.grupo5.tpo.marketplace.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.grupo5.tpo.marketplace.exception.BadRequestException;
import com.grupo5.tpo.marketplace.exception.ResourceNotFoundException;
import com.grupo5.tpo.marketplace.model.Favorite;
import com.grupo5.tpo.marketplace.model.FavoriteId;
import com.grupo5.tpo.marketplace.model.Product;
import com.grupo5.tpo.marketplace.model.User;
import com.grupo5.tpo.marketplace.repository.FavoriteRepository;
import com.grupo5.tpo.marketplace.repository.ProductRepository;
import com.grupo5.tpo.marketplace.repository.UserRepository;

@Service
public class FavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getFavorites(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        return favoriteRepository.findByIdUserId(user.getId()).stream()
                .map(Favorite::getProduct)
                .collect(Collectors.toList());
    }

    @Transactional
    public void addFavorite(String username, Long productId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));

        if (favoriteRepository.existsByIdUserIdAndIdProductId(user.getId(), productId)) {
            throw new BadRequestException("El producto ya esta en favoritos");
        }

        Favorite favorite = new Favorite();
        favorite.setId(new FavoriteId(user.getId(), productId));
        favorite.setUser(user);
        favorite.setProduct(product);
        favoriteRepository.save(favorite);
    }

    @Transactional
    public void removeFavorite(String username, Long productId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        if (!favoriteRepository.existsByIdUserIdAndIdProductId(user.getId(), productId)) {
            throw new ResourceNotFoundException("El producto no esta en favoritos");
        }

        favoriteRepository.deleteByIdUserIdAndIdProductId(user.getId(), productId);
    }

    public boolean isFavorite(String username, Long productId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        return favoriteRepository.existsByIdUserIdAndIdProductId(user.getId(), productId);
    }
}
