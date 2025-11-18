package com.rokefeli.colmenares.api.service.interfaces;

import com.rokefeli.colmenares.api.dto.auth.*;
import com.rokefeli.colmenares.api.dto.create.UsuarioCreateDTO;

public interface AuthService {
    AuthResponse registerCliente(UsuarioCreateDTO request)
    AuthResponse registrarCliente(UsuarioCreateDTO request);
}
