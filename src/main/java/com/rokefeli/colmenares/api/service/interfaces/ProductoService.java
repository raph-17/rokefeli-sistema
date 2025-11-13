package com.rokefeli.colmenares.api.service.interfaces;

import java.util.List;

import com.rokefeli.colmenares.api.dto.create.ProductoCreateDTO;
import com.rokefeli.colmenares.api.dto.response.ProductoResponseDTO;
import com.rokefeli.colmenares.api.dto.update.ProductoUpdateDTO;

public interface ProductoService {
    List<ProductoResponseDTO> findAll();
    ProductoResponseDTO findById(Long id);
    ProductoResponseDTO create(ProductoCreateDTO createDTO);
    ProductoResponseDTO update(Long id, ProductoUpdateDTO updateDTO);
    void softDelete(Long id);
    void hardDelete(Long id);
}
