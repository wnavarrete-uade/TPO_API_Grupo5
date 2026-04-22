package com.grupo5.tpo.marketplace.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.grupo5.tpo.marketplace.model.Favorite;
import com.grupo5.tpo.marketplace.model.FavoriteId;

public interface FavoriteRepository extends JpaRepository<Favorite, FavoriteId> {
    List<Favorite> findByIdUserId(Long userId);
    boolean existsByIdUserIdAndIdProductId(Long userId, Long productId);
    void deleteByIdUserIdAndIdProductId(Long userId, Long productId);
}
