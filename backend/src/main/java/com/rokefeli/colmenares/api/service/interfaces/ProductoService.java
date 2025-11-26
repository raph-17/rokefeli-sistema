package com.rokefeli.colmenares.api.service.interfaces;

import java.util.List;

import com.rokefeli.colmenares.api.dto.create.ProductoCreateDTO;
import com.rokefeli.colmenares.api.dto.response.ProductoResponseDTO;
import com.rokefeli.colmenares.api.dto.update.ProductoUpdateDTO;
import com.rokefeli.colmenares.api.dto.update.StockAdjustmentDTO;
import com.rokefeli.colmenares.api.entity.enums.EstadoProducto;

public interface ProductoService {
    List<ProductoResponseDTO> findAll();
    List<ProductoResponseDTO> findAllActivos();
    ProductoResponseDTO findById(Long id);
    ProductoResponseDTO findByIdCliente(Long id);
    List<ProductoResponseDTO> buscarCliente(String nombre, Long idCategoria);
    List<ProductoResponseDTO> buscarAdmin(String nombre, Long idCategoria, EstadoProducto estado);
    ProductoResponseDTO create(ProductoCreateDTO createDTO);
    ProductoResponseDTO update(Long id, ProductoUpdateDTO updateDTO);
    void ajustarStock(StockAdjustmentDTO adjustmentDTO);
    void desactivar(Long id);
    void activar(Long id);
    void delete(Long id);
}
