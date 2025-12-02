package com.rokefeli.colmenares.api.dto.response;

import com.rokefeli.colmenares.api.entity.enums.CanalVenta;
import com.rokefeli.colmenares.api.entity.enums.EstadoVenta;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class VentaResponseDTO {
    private Long id;
    private Long idUsuario;
    private String nombresUsuario;
    private String apellidosUsuario;
    private String dniUsuario;
    private CanalVenta canal;
    private BigDecimal montoTotal;
    private EstadoVenta estado;
    private LocalDateTime fecha;
    private List<DetalleVentaResponseDTO> detalles;
}
