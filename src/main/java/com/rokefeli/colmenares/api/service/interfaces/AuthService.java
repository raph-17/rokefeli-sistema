package com.rokefeli.colmenares.api.service.interfaces;

import com.rokefeli.colmenares.api.dto.auth.*;
import com.rokefeli.colmenares.api.dto.create.AdminCreateDTO;
import com.rokefeli.colmenares.api.dto.create.UsuarioCreateDTO;

public interface AuthService {
    AuthResponse login(AuthRequest request);
    AuthResponse registrarCliente(UsuarioCreateDTO dto);
    AuthResponse registrarAdmin(AdminCreateDTO dto);
}
