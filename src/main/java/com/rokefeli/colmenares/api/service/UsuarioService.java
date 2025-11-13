package com.rokefeli.colmenares.api.service;

import java.util.List;

import com.rokefeli.colmenares.api.dto.create.UsuarioCreateDTO;
import com.rokefeli.colmenares.api.dto.response.UsuarioResponseDTO;
import com.rokefeli.colmenares.api.dto.update.UsuarioUpdateDTO;

public interface UsuarioService {
    List<UsuarioResponseDTO> findAll();
    UsuarioResponseDTO findById(Long id);
    UsuarioResponseDTO create(UsuarioCreateDTO createDTO);
    UsuarioResponseDTO update(Long id, UsuarioUpdateDTO updateDTO);
    void delete(Long id);
}
