package com.simplesdental.product.service;

import com.simplesdental.product.model.Category;
import com.simplesdental.product.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Transactional
    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    @Transactional
    public Category save(Category category) {
        validateCategory(category);
        return categoryRepository.save(category);
    }

    @Transactional
    public Optional<Category> update(Long id, Category category) {
        return categoryRepository.findById(id)
                .map(existingCategory -> {
                    category.setId(id);
                    validateCategory(category);
                    return categoryRepository.save(category);
                });
    }

    @Transactional
    public Optional<Void> deleteById(Long id) {
        return categoryRepository.findById(id)
                .map(category -> {
                    categoryRepository.deleteById(id);
                    return null;
                });
    }

    private void validateCategory(Category category) {
        if (category.getName() == null || category.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome da categoria é obrigatório");
        }
        if (category.getName().length() > 100) {
            throw new IllegalArgumentException("O nome da categoria deve ter no máximo 100 caracteres");
        }
        if (category.getDescription() != null && category.getDescription().length() > 255) {
            throw new IllegalArgumentException("A descrição da categoria deve ter no máximo 255 caracteres");
        }
    }
}