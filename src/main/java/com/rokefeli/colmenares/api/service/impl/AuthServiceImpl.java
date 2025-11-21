package com.rokefeli.colmenares.api.service.impl;

import com.rokefeli.colmenares.api.dto.auth.*;
import com.rokefeli.colmenares.api.dto.create.AdminCreateDTO;
import com.rokefeli.colmenares.api.dto.create.UsuarioCreateDTO;
import com.rokefeli.colmenares.api.entity.Usuario;
import com.rokefeli.colmenares.api.entity.enums.EstadoUsuario;
import com.rokefeli.colmenares.api.entity.enums.Rol;
import com.rokefeli.colmenares.api.exception.ResourceNotFoundException;
import com.rokefeli.colmenares.api.mapper.UsuarioMapper;
import com.rokefeli.colmenares.api.repository.UsuarioRepository;
import com.rokefeli.colmenares.api.security.JwtUtil;
import com.rokefeli.colmenares.api.service.interfaces.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UsuarioMapper usuarioMapper;

    @Override
    @Transactional
    public AuthResponse registrarCliente(UsuarioCreateDTO dto) {
        if (usuarioRepository.existsByEmailIgnoreCase(dto.getEmail())) {
            throw new IllegalArgumentException("El email ya está en uso");
        }
        Usuario u = usuarioMapper.toEntity(dto);
        u.setPassword(passwordEncoder.encode(dto.getPassword()));
        u.setRol(Rol.CLIENTE);
        u.setEstado(EstadoUsuario.ACTIVO);

        Usuario saved = usuarioRepository.save(u);

        String token = jwtUtil.generateToken(saved.getId(), saved.getEmail(), saved.getRol().name());
        return new AuthResponse(token, saved.getId(), saved.getNombres(), saved.getRol().name(), "Bearer");

    }

    @Override
    @Transactional
    public AuthResponse registrarAdmin(AdminCreateDTO dto) {
        if (usuarioRepository.existsByEmailIgnoreCase(dto.getEmail())) {
            throw new IllegalArgumentException("El email ya está en uso");
        }
        Usuario u = usuarioMapper.toEntity(dto);
        u.setPassword(passwordEncoder.encode(dto.getPassword()));
        u.setEstado(EstadoUsuario.ACTIVO);

        Usuario saved = usuarioRepository.save(u);

        String token = jwtUtil.generateToken(saved.getId(), saved.getEmail(), saved.getRol().name());
        return new AuthResponse(token, saved.getId(), saved.getNombres(), saved.getRol().name(), "Bearer");

    }

    @Override
    public AuthResponse login(AuthRequest request) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        UserDetails principal = (UserDetails) auth.getPrincipal();
        // recupera usuario para id y rol
        Usuario u = usuarioRepository.findByEmail(principal.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", principal.getUsername()));
        String token = jwtUtil.generateToken(u.getId(), u.getEmail(), u.getRol().name());
        return new AuthResponse(token, u.getId(), u.getNombres(), u.getRol().name(), "Bearer");
    }
}
