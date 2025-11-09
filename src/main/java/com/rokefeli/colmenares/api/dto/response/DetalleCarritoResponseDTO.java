package com.rokefeli.colmenares.api.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DetalleCarritoResponseDTO {
    private Long idProducto;
    private String nombreProducto;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
}
