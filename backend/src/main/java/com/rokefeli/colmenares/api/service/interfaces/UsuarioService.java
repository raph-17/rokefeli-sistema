package com.rokefeli.colmenares.api.service.interfaces;

import java.util.List;

import com.rokefeli.colmenares.api.dto.create.AdminCreateDTO;
import com.rokefeli.colmenares.api.dto.create.UsuarioCreateDTO;
import com.rokefeli.colmenares.api.dto.response.UsuarioResponseDTO;
import com.rokefeli.colmenares.api.dto.update.AdminUpdateDTO;
import com.rokefeli.colmenares.api.dto.update.PasswordChangeDTO;
import com.rokefeli.colmenares.api.dto.update.UsuarioUpdateDTO;
import com.rokefeli.colmenares.api.entity.enums.EstadoUsuario;

public interface UsuarioService {
    List<UsuarioResponseDTO> findAll();
    UsuarioResponseDTO findById(Long id);
    List<UsuarioResponseDTO> findByEstado(EstadoUsuario estado);
    UsuarioResponseDTO findByDni(String dni);
    UsuarioResponseDTO updateUsuario(Long id, UsuarioUpdateDTO updateDTO);
    UsuarioResponseDTO updateAdmin(Long id, AdminUpdateDTO updateDTO);
    void cambiarPassword(Long id, PasswordChangeDTO dto);
    void delete(Long id);
    void cambiarEstado(Long id, EstadoUsuario nuevoEstado);
}
