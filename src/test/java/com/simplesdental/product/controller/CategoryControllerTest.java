package com.simplesdental.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simplesdental.product.model.Category;
import com.simplesdental.product.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @Autowired
    private ObjectMapper objectMapper;

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
    void findAll_ShouldReturnPageOfCategories() throws Exception {
        // Arrange
        List<Category> categories = Arrays.asList(testCategory);
        Page<Category> page = new PageImpl<>(categories, PageRequest.of(0, 10), categories.size());
        when(categoryService.findAll(any(PageRequest.class))).thenReturn(page);

        // Act & Assert
        mockMvc.perform(get("/api/categories")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(TEST_ID))
                .andExpect(jsonPath("$.content[0].name").value(TEST_NAME))
                .andExpect(jsonPath("$.content[0].description").value(TEST_DESCRIPTION));
    }

    @Test
    void findById_WithValidId_ShouldReturnCategory() throws Exception {
        // Arrange
        when(categoryService.findById(TEST_ID)).thenReturn(testCategory);

        // Act & Assert
        mockMvc.perform(get("/api/categories/{id}", TEST_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(TEST_ID))
                .andExpect(jsonPath("$.name").value(TEST_NAME))
                .andExpect(jsonPath("$.description").value(TEST_DESCRIPTION));
    }

    @Test
    void findById_WithInvalidId_ShouldReturnNotFound() throws Exception {
        // Arrange
        when(categoryService.findById(TEST_ID)).thenThrow(new RuntimeException("Category not found"));

        // Act & Assert
        mockMvc.perform(get("/api/categories/{id}", TEST_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void create_WithValidCategory_ShouldReturnCreatedCategory() throws Exception {
        // Arrange
        when(categoryService.save(any(Category.class))).thenReturn(testCategory);

        // Act & Assert
        mockMvc.perform(post("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCategory)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(TEST_ID))
                .andExpect(jsonPath("$.name").value(TEST_NAME))
                .andExpect(jsonPath("$.description").value(TEST_DESCRIPTION));
    }

    @Test
    void update_WithValidCategory_ShouldReturnUpdatedCategory() throws Exception {
        // Arrange
        when(categoryService.update(eq(TEST_ID), any(Category.class))).thenReturn(testCategory);

        // Act & Assert
        mockMvc.perform(put("/api/categories/{id}", TEST_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCategory)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(TEST_ID))
                .andExpect(jsonPath("$.name").value(TEST_NAME))
                .andExpect(jsonPath("$.description").value(TEST_DESCRIPTION));
    }

    @Test
    void update_WithInvalidId_ShouldReturnNotFound() throws Exception {
        // Arrange
        when(categoryService.update(eq(TEST_ID), any(Category.class)))
                .thenThrow(new RuntimeException("Category not found"));

        // Act & Assert
        mockMvc.perform(put("/api/categories/{id}", TEST_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCategory)))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_WithValidId_ShouldReturnNoContent() throws Exception {
        // Arrange
        doNothing().when(categoryService).delete(TEST_ID);

        // Act & Assert
        mockMvc.perform(delete("/api/categories/{id}", TEST_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_WithInvalidId_ShouldReturnNotFound() throws Exception {
        // Arrange
        doThrow(new RuntimeException("Category not found")).when(categoryService).delete(TEST_ID);

        // Act & Assert
        mockMvc.perform(delete("/api/categories/{id}", TEST_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
} 