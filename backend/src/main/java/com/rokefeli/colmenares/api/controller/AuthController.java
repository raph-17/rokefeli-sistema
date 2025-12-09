package com.rokefeli.colmenares.api.controller;

import com.rokefeli.colmenares.api.dto.auth.*;
import com.rokefeli.colmenares.api.dto.create.AdminCreateDTO;
import com.rokefeli.colmenares.api.dto.create.UsuarioCreateDTO;
import com.rokefeli.colmenares.api.service.interfaces.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    // Registrar usuario
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registrarCliente(@Valid @RequestBody UsuarioCreateDTO request) {
        AuthResponse response = authService.registrarCliente(request);
        return ResponseEntity.ok(response);
    }

    // ADMIN: Registrar usuario ADMIN o EMPLEADO
    @PostMapping("/register/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AuthResponse> registrarAdmin(@Valid @RequestBody AdminCreateDTO request) {
        AuthResponse response = authService.registrarAdmin(request);
        return ResponseEntity.ok(response);
    }

    // Login
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}
