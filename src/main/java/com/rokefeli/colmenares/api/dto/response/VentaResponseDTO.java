package com.rokefeli.colmenares.api.dto.response;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class VentaResponseDTO {
    private Long id;
    private Long idUsuario;
    private String nombreUsuario;
    private BigDecimal montoTotal;
    private LocalDateTime fecha;
    private List<DetalleVentaResponseDTO> detalles;
}
