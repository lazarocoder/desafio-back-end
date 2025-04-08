package com.simplesdental.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simplesdental.product.model.Product;
import com.simplesdental.product.service.ProductService;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    private Product testProduct;
    private static final Long TEST_ID = 1L;
    private static final String TEST_NAME = "Test Product";
    private static final String TEST_DESCRIPTION = "Test Description";
    private static final Double TEST_PRICE = 100.0;

    @BeforeEach
    void setUp() {
        testProduct = new Product();
        testProduct.setId(TEST_ID);
        testProduct.setName(TEST_NAME);
        testProduct.setDescription(TEST_DESCRIPTION);
        testProduct.setPrice(TEST_PRICE);
    }

    @Test
    void findAll_ShouldReturnPageOfProducts() throws Exception {
        // Arrange
        List<Product> products = Arrays.asList(testProduct);
        Page<Product> page = new PageImpl<>(products, PageRequest.of(0, 10), products.size());
        when(productService.findAll(any(PageRequest.class))).thenReturn(page);

        // Act & Assert
        mockMvc.perform(get("/api/products")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(TEST_ID))
                .andExpect(jsonPath("$.content[0].name").value(TEST_NAME))
                .andExpect(jsonPath("$.content[0].description").value(TEST_DESCRIPTION))
                .andExpect(jsonPath("$.content[0].price").value(TEST_PRICE));
    }

    @Test
    void findById_WithValidId_ShouldReturnProduct() throws Exception {
        // Arrange
        when(productService.findById(TEST_ID)).thenReturn(testProduct);

        // Act & Assert
        mockMvc.perform(get("/api/products/{id}", TEST_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(TEST_ID))
                .andExpect(jsonPath("$.name").value(TEST_NAME))
                .andExpect(jsonPath("$.description").value(TEST_DESCRIPTION))
                .andExpect(jsonPath("$.price").value(TEST_PRICE));
    }

    @Test
    void findById_WithInvalidId_ShouldReturnNotFound() throws Exception {
        // Arrange
        when(productService.findById(TEST_ID)).thenThrow(new RuntimeException("Product not found"));

        // Act & Assert
        mockMvc.perform(get("/api/products/{id}", TEST_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void create_WithValidProduct_ShouldReturnCreatedProduct() throws Exception {
        // Arrange
        when(productService.save(any(Product.class))).thenReturn(testProduct);

        // Act & Assert
        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testProduct)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(TEST_ID))
                .andExpect(jsonPath("$.name").value(TEST_NAME))
                .andExpect(jsonPath("$.description").value(TEST_DESCRIPTION))
                .andExpect(jsonPath("$.price").value(TEST_PRICE));
    }

    @Test
    void update_WithValidProduct_ShouldReturnUpdatedProduct() throws Exception {
        // Arrange
        when(productService.update(eq(TEST_ID), any(Product.class))).thenReturn(testProduct);

        // Act & Assert
        mockMvc.perform(put("/api/products/{id}", TEST_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testProduct)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(TEST_ID))
                .andExpect(jsonPath("$.name").value(TEST_NAME))
                .andExpect(jsonPath("$.description").value(TEST_DESCRIPTION))
                .andExpect(jsonPath("$.price").value(TEST_PRICE));
    }

    @Test
    void update_WithInvalidId_ShouldReturnNotFound() throws Exception {
        // Arrange
        when(productService.update(eq(TEST_ID), any(Product.class)))
                .thenThrow(new RuntimeException("Product not found"));

        // Act & Assert
        mockMvc.perform(put("/api/products/{id}", TEST_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testProduct)))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_WithValidId_ShouldReturnNoContent() throws Exception {
        // Arrange
        doNothing().when(productService).delete(TEST_ID);

        // Act & Assert
        mockMvc.perform(delete("/api/products/{id}", TEST_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_WithInvalidId_ShouldReturnNotFound() throws Exception {
        // Arrange
        doThrow(new RuntimeException("Product not found")).when(productService).delete(TEST_ID);

        // Act & Assert
        mockMvc.perform(delete("/api/products/{id}", TEST_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}