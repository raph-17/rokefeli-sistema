package com.rokefeli.colmenares.api.controller;

import com.rokefeli.colmenares.api.dto.response.UsuarioResponseDTO;
import com.rokefeli.colmenares.api.dto.update.AdminUpdateDTO;
import com.rokefeli.colmenares.api.dto.update.PasswordChangeDTO;
import com.rokefeli.colmenares.api.dto.update.UsuarioUpdateDTO;
import com.rokefeli.colmenares.api.entity.enums.EstadoUsuario;
import com.rokefeli.colmenares.api.service.interfaces.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // ADMIN: Obtener todos los usuarios
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UsuarioResponseDTO> findAll() {
        return usuarioService.findAll();
    }

    // ADMIN: Obtener por estado
    @GetMapping("/estado")
    @PreAuthorize("hasRole('ADMIN')")
    public List<UsuarioResponseDTO> findByEstado(
            @RequestParam EstadoUsuario estado
    ) {
        return usuarioService.findByEstado(estado);
    }

    // ADMIN: Obtener por DNI
    @GetMapping("/dni")
    @PreAuthorize("hasRole('ADMIN')")
    public UsuarioResponseDTO findByDni(
            @RequestParam String dni
    ) {
        return usuarioService.findByDni(dni);
    }

    // ADMIN: Puede ver a cualquiera
    // CLIENTE: Solo puede ver su propio usuario
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isSelf(authentication, #id)")
    public UsuarioResponseDTO findById(@PathVariable Long id) {
        return usuarioService.findById(id);
    }

    // ADMIN: Edita cualquier usuario
    // CLIENTE: Edita solo su perfil
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isSelf(authentication, #id)")
    public UsuarioResponseDTO updateUsuario(
            @PathVariable Long id,
            @RequestBody UsuarioUpdateDTO dto
    ) {
        return usuarioService.updateUsuario(id, dto);
    }

    // ADMIN: Edita datos sensibles de cualquier usuario (DNI)
    @PutMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UsuarioResponseDTO updateAdmin(
            @PathVariable Long id,
            @RequestBody AdminUpdateDTO dto
    ) {
        return usuarioService.updateAdmin(id, dto);
    }

    // Solo el usuario puede cambiar su propia contraseña
    @PutMapping("/{id}/password")
    @PreAuthorize("@securityService.isSelf(authentication, #id)")
    public void cambiarPassword(
            @PathVariable Long id,
            @RequestBody PasswordChangeDTO dto
    ) {
        usuarioService.cambiarPassword(id, dto);
    }

    // ADMIN: Cambia el estado (ACTIVO / INACTIVO)
    @PutMapping("/{id}/estado")
    @PreAuthorize("hasRole('ADMIN')")
    public void cambiarEstado(
            @PathVariable Long id,
            @RequestParam EstadoUsuario estado
    ) {
        usuarioService.cambiarEstado(id, estado);
    }

    // ADMIN: Elimina usuarios
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        usuarioService.delete(id);
    }
}
