package com.rokefeli.colmenares.api.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductoResponseDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private String categoriaNombre;
    private Long categoriaId;
    private String estado;
    private String imagenUrl;
    private Integer stockActual;
    private Integer stockMinimo;
    private BigDecimal precio;
}
