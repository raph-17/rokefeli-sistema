package com.rokefeli.colmenares.api.service.impl;

import com.rokefeli.colmenares.api.dto.auth.*;
import com.rokefeli.colmenares.api.dto.create.UsuarioCreateDTO;
import com.rokefeli.colmenares.api.entity.Usuario;
import com.rokefeli.colmenares.api.entity.enums.EstadoUsuario;
import com.rokefeli.colmenares.api.entity.enums.Rol;
import com.rokefeli.colmenares.api.exception.ResourceNotFoundException;
import com.rokefeli.colmenares.api.repository.UsuarioRepository;
import com.rokefeli.colmenares.api.security.JwtUtil;
import com.rokefeli.colmenares.api.service.interfaces.AuthService;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authManager;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthServiceImpl(AuthenticationManager authManager,
                           UsuarioRepository usuarioRepository,
                           PasswordEncoder passwordEncoder,
                           JwtUtil jwtUtil) {
        this.authManager = authManager;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    @Transactional
    public AuthResponse registerCliente(UsuarioCreateDTO request) {
        if (usuarioRepository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new IllegalArgumentException("El email ya está en uso");
        }
        Usuario u = new Usuario();
        u.setNombres(request.getNombres());
        u.setApellidos(request.getApellidos());
        u.setDni(request.getDni());
        u.setEmail(request.getEmail());
        u.setPassword(passwordEncoder.encode(request.getPassword()));
        u.setRol(Rol.CLIENTE); // o el enum que uses
        u.setEstado(EstadoUsuario.ACTIVO);
        usuarioRepository.save(u);

        String token = jwtUtil.generateToken(u.getId(), u.getEmail(), u.getRol().name());
        return new AuthResponse(token, "Bearer");
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) auth.getPrincipal();
        // recupera usuario para id y rol
        Usuario u = usuarioRepository.findByEmail(principal.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", principal.getUsername()));
        String token = jwtUtil.generateToken(u.getId(), u.getEmail(), u.getRol().name());
        return new AuthResponse(token, "Bearer");
    }
}
