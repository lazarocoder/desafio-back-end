package com.simplesdental.product.service;

import com.simplesdental.product.model.Product;
import com.simplesdental.product.repository.ProductRepository;
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

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product product;
    private static final Long TEST_ID = 1L;
    private static final String TEST_NAME = "Test Product";
    private static final String TEST_DESCRIPTION = "Test Description";
    private static final Double TEST_PRICE = 100.0;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(TEST_ID);
        product.setName(TEST_NAME);
        product.setDescription(TEST_DESCRIPTION);
        product.setPrice(TEST_PRICE);
        product.setStatus(true);
        product.setCode("TP001");
    }

    @Test
    void shouldSaveProduct() {
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product savedProduct = productService.save(product);

        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getId()).isEqualTo(TEST_ID);
        assertThat(savedProduct.getName()).isEqualTo(TEST_NAME);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void shouldGetAllProducts() {
        when(productRepository.findAll()).thenReturn(Arrays.asList(product));

        List<Product> products = productService.findAll();

        assertThat(products).isNotNull();
        assertThat(products.size()).isEqualTo(1);
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void shouldGetProductById() {
        when(productRepository.findById(TEST_ID)).thenReturn(Optional.of(product));

        Optional<Product> foundProduct = productService.findById(TEST_ID);

        assertThat(foundProduct).isPresent();
        assertThat(foundProduct.get().getId()).isEqualTo(TEST_ID);
        assertThat(foundProduct.get().getName()).isEqualTo(TEST_NAME);
        verify(productRepository, times(1)).findById(TEST_ID);
    }

    @Test
    void shouldDeleteProductById() {
        doNothing().when(productRepository).deleteById(TEST_ID);

        productService.deleteById(TEST_ID);

        verify(productRepository, times(1)).deleteById(TEST_ID);
    }

    @Test
    void findAll_ShouldReturnPageOfProducts() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        List<Product> products = Arrays.asList(product);
        Page<Product> expectedPage = new PageImpl<>(products, pageable, products.size());
        
        when(productRepository.findAll(pageable)).thenReturn(expectedPage);

        // Act
        Page<Product> result = productService.findAll(pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(TEST_NAME, result.getContent().get(0).getName());
    }

    @Test
    void findById_WithInvalidId_ShouldThrowException() {
        // Arrange
        when(productRepository.findById(TEST_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> 
            productService.findById(TEST_ID)
        );
    }

    @Test
    void update_WithValidProduct_ShouldReturnUpdatedProduct() {
        // Arrange
        when(productRepository.findById(TEST_ID)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product updatedProduct = new Product();
        updatedProduct.setId(TEST_ID);
        updatedProduct.setName("Updated Name");
        updatedProduct.setDescription("Updated Description");
        updatedProduct.setPrice(200.0);

        // Act
        Product result = productService.update(TEST_ID, updatedProduct);

        // Assert
        assertNotNull(result);
        assertEquals(TEST_ID, result.getId());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void update_WithInvalidId_ShouldThrowException() {
        // Arrange
        when(productRepository.findById(TEST_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> 
            productService.update(TEST_ID, product)
        );
    }

    @Test
    void delete_WithInvalidId_ShouldThrowException() {
        // Arrange
        when(productRepository.findById(TEST_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> 
            productService.delete(TEST_ID)
        );
    }
}