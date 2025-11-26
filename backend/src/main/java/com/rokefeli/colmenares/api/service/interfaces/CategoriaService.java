package com.rokefeli.colmenares.api.service.interfaces;

import java.util.List;

import com.rokefeli.colmenares.api.dto.create.CategoriaCreateDTO;
import com.rokefeli.colmenares.api.dto.response.CategoriaResponseDTO;
import com.rokefeli.colmenares.api.dto.update.CategoriaUpdateDTO;
import com.rokefeli.colmenares.api.entity.enums.EstadoCategoria;

public interface CategoriaService {
    List<CategoriaResponseDTO> findAll();
    List<CategoriaResponseDTO> findAllActivos();
    List<CategoriaResponseDTO> findByEstado(EstadoCategoria estado);
    CategoriaResponseDTO findById(Long id);
    CategoriaResponseDTO findByIdCliente(Long id);
    List<CategoriaResponseDTO> findByNameContainingIgnoreCaseAdmin(String name);
    List<CategoriaResponseDTO> findByNameContainingIgnoreCaseCliente(String name);
    CategoriaResponseDTO create(CategoriaCreateDTO createDTO);
    CategoriaResponseDTO update(Long id, CategoriaUpdateDTO updateDTO);
    void desactivar(Long id);
    void activar(Long id);
    void delete(Long id);
}
