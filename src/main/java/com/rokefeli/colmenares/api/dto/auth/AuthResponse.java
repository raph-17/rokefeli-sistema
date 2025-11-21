package com.rokefeli.colmenares.api.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private Long idUsuario;
    private String nombres;
    private String rol;
    private String tipo;
}
