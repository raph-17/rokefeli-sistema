package com.rokefeli.colmenares.api.dto.response;

import com.rokefeli.colmenares.api.entity.enums.CanalVenta;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class VentaResponseDTO {
    private Long id;
    private Long idUsuario;
    private String nombreUsuario;
    private CanalVenta canal;
    private BigDecimal montoTotal;
    private LocalDateTime fecha;
    private List<DetalleVentaResponseDTO> detalles;
}
