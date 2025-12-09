package com.rokefeli.colmenares.api.service.interfaces;

import com.rokefeli.colmenares.api.dto.create.DetalleCarritoCreateDTO;
import com.rokefeli.colmenares.api.dto.response.CarritoResponseDTO;

public interface CarritoService {
    CarritoResponseDTO verCarrito(Long idUsuario);
    CarritoResponseDTO agregarProducto(Long idUsuario, DetalleCarritoCreateDTO dto);
    CarritoResponseDTO actualizarCantidad(Long idUsuario, Long idProducto, Integer nuevaCantidad);
    CarritoResponseDTO eliminarProducto(Long idUsuario, Long idProducto);
    void vaciarCarrito(Long idUsuario);
    void marcarComoComprado(Long idUsuario);
}

