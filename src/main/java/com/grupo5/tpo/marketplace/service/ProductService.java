package com.grupo5.tpo.marketplace.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.grupo5.tpo.marketplace.dto.ProductRequest;
import com.grupo5.tpo.marketplace.exception.BadRequestException;
import com.grupo5.tpo.marketplace.exception.ResourceNotFoundException;
import com.grupo5.tpo.marketplace.model.Category;
import com.grupo5.tpo.marketplace.model.Product;
import com.grupo5.tpo.marketplace.model.User;
import com.grupo5.tpo.marketplace.repository.CategoryRepository;
import com.grupo5.tpo.marketplace.repository.ProductRepository;
import com.grupo5.tpo.marketplace.repository.UserRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Product> getAll() {
        return productRepository.findAll();
    }

    public Product getById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));
    }

    public List<Product> getByCategoryId(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    public List<Product> getBySellerId(Long sellerId) {
        return productRepository.findBySellerId(sellerId);
    }

    public List<Product> search(String query) {
        return productRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(query, query);
    }

    public Product create(ProductRequest request, String sellerUsername) {
        User seller = userRepository.findByUsername(sellerUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Vendedor no encontrado"));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada"));

        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setDiscount(request.getDiscount() != null ? request.getDiscount() : 0.0);
        product.setStock(request.getStock());
        product.setImageUrl(request.getImageUrl());
        product.setCategory(category);
        product.setSeller(seller);

        return productRepository.save(product);
    }

    public Product update(Long id, ProductRequest request, String sellerUsername) {
        Product product = getById(id);

        // Verificar que el vendedor sea el dueño del producto
        if (!product.getSeller().getUsername().equals(sellerUsername)) {
            throw new BadRequestException("No tenés permisos para modificar este producto");
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada"));

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setImageUrl(request.getImageUrl());
        product.setCategory(category);
        if (request.getDiscount() != null) {
            product.setDiscount(request.getDiscount());
        }

        return productRepository.save(product);
    }

    public void delete(Long id, String sellerUsername) {
        Product product = getById(id);
        if (!product.getSeller().getUsername().equals(sellerUsername)) {
            throw new BadRequestException("No tenés permisos para eliminar este producto");
        }
        productRepository.delete(product);
    }

    public Product updateStock(Long id, Integer stock, String sellerUsername) {
        Product product = getById(id);
        if (!product.getSeller().getUsername().equals(sellerUsername)) {
            throw new BadRequestException("No tenés permisos para modificar el stock");
        }
        product.setStock(stock);
        return productRepository.save(product);
    }

    public Product updateDiscount(Long id, Double discount, String sellerUsername) {
        Product product = getById(id);
        if (!product.getSeller().getUsername().equals(sellerUsername)) {
            throw new BadRequestException("No tenés permisos para modificar el descuento");
        }
        product.setDiscount(discount);
        return productRepository.save(product);
    }
}
