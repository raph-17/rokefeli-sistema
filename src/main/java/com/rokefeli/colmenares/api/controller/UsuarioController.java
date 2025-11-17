package com.rokofeli.colmenares.api.controller;


import com.rokefeli.colmenares.api.dto.response.UsuarioResponseDTO;
import com.rokefeli.colmenares.api.dto.update.UsuarioUpdateDTO;
import com.rokefeli.colmenares.api.service.interfaces.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    /**
     * Endpoint para que el usuario CLIENTE obtenga su propia información de perfil.
     * Requiere token JWT.
     */
    @GetMapping("/perfil")
    @Secured({"ROLE_CLIENTE", "ROLE_ADMIN", "ROLE_EMPLEADO"})
    public UsuarioResponseDTO obtenerPerfilUsuarioActual() {
        // La lógica para obtener el ID/Email del usuario actual está dentro del servicio (con SecurityContextHolder)
        return usuarioService.obtenerPerfilUsuarioActual();
    }

    /**
     * Endpoint para que el CLIENTE o ADMIN actualice su propia información.
     * Requiere token JWT.
     */
    @PutMapping("/perfil")
    @Secured({"ROLE_CLIENTE", "ROLE_ADMIN", "ROLE_EMPLEADO"})
    public UsuarioResponseDTO actualizarPerfilUsuarioActual(@RequestBody UsuarioUpdateDTO dto) {
        // El servicio obtendrá el ID del usuario actual para asegurar que solo edita su propio perfil.
        return usuarioService.actualizarPerfilUsuarioActual(dto);
    }

    /**
     * Endpoint para que un ADMIN obtenga la información de CUALQUIER usuario por ID.
     * Restringido solo a administradores.
     */
    @GetMapping("/{id}")
    @Secured({"ROLE_ADMIN", "ROLE_EMPLEADO"})
    public UsuarioResponseDTO obtenerUsuarioPorId(@PathVariable Long id) {
        return usuarioService.findById(id);
    }
}