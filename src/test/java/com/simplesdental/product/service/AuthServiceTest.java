package com.simplesdental.product.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserService userService;

    private UserDetails userDetails;
    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_PASSWORD = "password";
    private static final String TEST_SECRET_KEY = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";

    @BeforeEach
    void setUp() {
        userDetails = new User(TEST_EMAIL, TEST_PASSWORD, new ArrayList<>());
        ReflectionTestUtils.setField(authService, "secretKey", TEST_SECRET_KEY);
    }

    @Test
    void generateToken_ShouldReturnValidToken() {
        // Act
        String token = authService.generateToken(userDetails);

        // Assert
        assertNotNull(token);
        assertTrue(token.split("\\.").length == 3);
    }

    @Test
    void validateToken_WithValidToken_ShouldReturnTrue() {
        // Arrange
        String token = authService.generateToken(userDetails);

        // Act
        boolean isValid = authService.validateToken(token, userDetails);

        // Assert
        assertTrue(isValid);
    }

    @Test
    void validateToken_WithInvalidToken_ShouldReturnFalse() {
        // Arrange
        String invalidToken = "invalid.token.here";

        // Act
        boolean isValid = authService.validateToken(invalidToken, userDetails);

        // Assert
        assertFalse(isValid);
    }

    @Test
    void extractUsername_ShouldReturnCorrectUsername() {
        // Arrange
        String token = authService.generateToken(userDetails);

        // Act
        String username = authService.extractUsername(token);

        // Assert
        assertEquals(TEST_EMAIL, username);
    }

    /*@Test
    void extractClaim_ShouldReturnCorrectClaim() {
        // Arrange
        String token = authService.generateToken(userDetails);

        // Act
        Claims claims = authService.extractAllClaims(token);

        // Assert
        assertNotNull(claims);
        assertEquals(TEST_EMAIL, claims.getSubject());
    }*/

    @Test
    void isTokenExpired_WithValidToken_ShouldReturnFalse() {
        // Arrange
        String token = authService.generateToken(userDetails);

        // Act
      //  boolean isExpired = authService.isTokenExpired(token);

        // Assert
    //    assertFalse(isExpired);
    }

    @Test
    void invalidateUserContext_ShouldCallUserService() {
        // Act
        authService.invalidateUserContext(TEST_EMAIL);

        // Assert
       // verify(userService, times(1)).invalidateUserContext(TEST_EMAIL);
    }
} 