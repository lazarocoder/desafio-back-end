package com.simplesdental.product.service;

import com.simplesdental.product.model.Product;
import com.simplesdental.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.hibernate.Hibernate;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public List<Product> findAll() {
        List<Product> products = productRepository.findAll();
        products.forEach(product -> {
            if (product.getCategory() != null) {
                Hibernate.initialize(product.getCategory());
            }
        });
        return products;
    }

    @Transactional
    public Optional<Product> findById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        product.ifPresent(p -> {
            if (p.getCategory() != null) {
                Hibernate.initialize(p.getCategory());
            }
        });
        return product;
    }

    @Transactional
    public Product save(Product product) {
        validateProduct(product);
        return productRepository.save(product);
    }

    @Transactional
    public Optional<Product> update(Long id, Product product) {
        return productRepository.findById(id)
                .map(existingProduct -> {
                    product.setId(id);
                    validateProduct(product);
                    return productRepository.save(product);
                });
    }

    @Transactional
    public Optional<Void> deleteById(Long id) {
        return productRepository.findById(id)
                .map(product -> {
                    productRepository.deleteById(id);
                    return null;
                });
    }

    private void validateProduct(Product product) {
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do produto é obrigatório");
        }
        if (product.getName().length() > 100) {
            throw new IllegalArgumentException("O nome do produto deve ter no máximo 100 caracteres");
        }
        if (product.getDescription() != null && product.getDescription().length() > 255) {
            throw new IllegalArgumentException("A descrição do produto deve ter no máximo 255 caracteres");
        }
        if (product.getPrice() == null || product.getPrice().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O preço do produto é obrigatório e deve ser maior que zero");
        }
        if (product.getStatus() == null) {
            throw new IllegalArgumentException("O status do produto é obrigatório");
        }
        if (product.getCategory() == null) {
            throw new IllegalArgumentException("A categoria do produto é obrigatória");
        }
    }
}
