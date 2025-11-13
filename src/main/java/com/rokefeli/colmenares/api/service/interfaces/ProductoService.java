package com.rokefeli.colmenares.api.service.interfaces;

import java.util.List;

import com.rokefeli.colmenares.api.dto.create.ProductoCreateDTO;
import com.rokefeli.colmenares.api.dto.response.ProductoResponseDTO;
import com.rokefeli.colmenares.api.dto.update.ProductoUpdateDTO;
import com.rokefeli.colmenares.api.dto.update.StockAdjustmentDTO;
import com.rokefeli.colmenares.api.entity.enums.EstadoProducto;

public interface ProductoService {
    List<ProductoResponseDTO> findAll();
    ProductoResponseDTO findById(Long id);
    List<ProductoResponseDTO> findByCategoria(Long idCategoria);
    List<ProductoResponseDTO> findByEstado(EstadoProducto estado);
    List<ProductoResponseDTO> findByNombreContaining(String nombre);
    ProductoResponseDTO create(ProductoCreateDTO createDTO);
    ProductoResponseDTO update(Long id, ProductoUpdateDTO updateDTO);
    void ajustarStock(StockAdjustmentDTO adjustmentDTO);
    void descontinuar(Long id);
    void hardDelete(Long id);
}
