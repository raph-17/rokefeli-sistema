package com.rokefeli.colmenares.api.dto.response;

import com.rokefeli.colmenares.api.entity.enums.EstadoCarrito;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CarritoResponseDTO {
    private Long id;
    private Long idUsuario;
    private EstadoCarrito estado;
    private LocalDateTime fechaActualizacion;
    private List<DetalleCarritoResponseDTO> detalles;
    private BigDecimal total;
}