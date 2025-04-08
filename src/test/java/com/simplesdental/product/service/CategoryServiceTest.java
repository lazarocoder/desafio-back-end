package com.simplesdental.product.service;

import com.simplesdental.product.model.Category;
import com.simplesdental.product.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    private Category testCategory;
    private static final Long TEST_ID = 1L;
    private static final String TEST_NAME = "Test Category";
    private static final String TEST_DESCRIPTION = "Test Description";

    @BeforeEach
    void setUp() {
        testCategory = new Category();
        testCategory.setId(TEST_ID);
        testCategory.setName(TEST_NAME);
        testCategory.setDescription(TEST_DESCRIPTION);
    }

    @Test
    void findAll_ShouldReturnPageOfCategories() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        List<Category> categories = Arrays.asList(testCategory);
        Page<Category> expectedPage = new PageImpl<>(categories, pageable, categories.size());
        
        when(categoryRepository.findAll(pageable)).thenReturn(expectedPage);

        // Act
        Page<Category> result = categoryService.findAll(pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(TEST_NAME, result.getContent().get(0).getName());
    }

    @Test
    void findById_WithValidId_ShouldReturnCategory() {
        // Arrange
        when(categoryRepository.findById(TEST_ID)).thenReturn(Optional.of(testCategory));

        // Act
        Category result = categoryService.findById(TEST_ID);

        // Assert
        assertNotNull(result);
        assertEquals(TEST_ID, result.getId());
        assertEquals(TEST_NAME, result.getName());
    }

    @Test
    void findById_WithInvalidId_ShouldThrowException() {
        // Arrange
        when(categoryRepository.findById(TEST_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> 
            categoryService.findById(TEST_ID)
        );
    }

    @Test
    void save_WithValidCategory_ShouldReturnSavedCategory() {
        // Arrange
        when(categoryRepository.save(any(Category.class))).thenReturn(testCategory);

        // Act
        Category result = categoryService.save(testCategory);

        // Assert
        assertNotNull(result);
        assertEquals(TEST_ID, result.getId());
        assertEquals(TEST_NAME, result.getName());
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void update_WithValidCategory_ShouldReturnUpdatedCategory() {
        // Arrange
        when(categoryRepository.findById(TEST_ID)).thenReturn(Optional.of(testCategory));
        when(categoryRepository.save(any(Category.class))).thenReturn(testCategory);

        Category updatedCategory = new Category();
        updatedCategory.setId(TEST_ID);
        updatedCategory.setName("Updated Name");
        updatedCategory.setDescription("Updated Description");

        // Act
        Category result = categoryService.update(TEST_ID, updatedCategory);

        // Assert
        assertNotNull(result);
        assertEquals(TEST_ID, result.getId());
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void update_WithInvalidId_ShouldThrowException() {
        // Arrange
        when(categoryRepository.findById(TEST_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> 
            categoryService.update(TEST_ID, testCategory)
        );
    }

    @Test
    void delete_WithValidId_ShouldDeleteCategory() {
        // Arrange
        when(categoryRepository.findById(TEST_ID)).thenReturn(Optional.of(testCategory));
        doNothing().when(categoryRepository).deleteById(TEST_ID);

        // Act
        categoryService.delete(TEST_ID);

        // Assert
        verify(categoryRepository).deleteById(TEST_ID);
    }

    @Test
    void delete_WithInvalidId_ShouldThrowException() {
        // Arrange
        when(categoryRepository.findById(TEST_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> 
            categoryService.delete(TEST_ID)
        );
    }
} 