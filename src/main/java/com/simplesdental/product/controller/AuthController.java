package com.simplesdental.product.controller;

import com.simplesdental.product.dto.LoginRequest;
import com.simplesdental.product.dto.LoginResponse;
import com.simplesdental.product.dto.RegisterRequest;
import com.simplesdental.product.dto.UpdatePasswordRequest;
import com.simplesdental.product.model.User;
import com.simplesdental.product.service.AuthService;
import com.simplesdental.product.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação", description = "APIs de autenticação e gerenciamento de usuários")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final AuthService authService;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager, AuthService authService, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/login")
    @Operation(summary = "Realizar login", description = "Autentica um usuário e retorna um token JWT")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = authService.generateToken(userService.loadUserByUsername(request.getEmail()));
        User user = userService.getCurrentUser();

        return ResponseEntity.ok(new LoginResponse(jwt, user.getId(), user.getEmail(), user.getRole()));
    }

    @PostMapping("/register")
    @Operation(summary = "Registrar usuário", description = "Cria um novo usuário no sistema")
    public ResponseEntity<User> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(userService.register(request));
    }

    @GetMapping("/context")
    @Operation(summary = "Obter contexto do usuário", description = "Retorna os dados do usuário autenticado")
    public ResponseEntity<User> getContext() {
        return ResponseEntity.ok(userService.getCurrentUser());
    }

    @PutMapping("/password")
    @Operation(summary = "Atualizar senha", description = "Atualiza a senha do usuário autenticado")
    public ResponseEntity<Void> updatePassword(@Valid @RequestBody UpdatePasswordRequest request) {
        userService.updatePassword(request);
        return ResponseEntity.ok().build();
    }
} 