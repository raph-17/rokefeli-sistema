package com.rokefeli.colmenares.api.dto.response;

import com.rokefeli.colmenares.api.entity.enums.EstadoPedido;
import com.rokefeli.colmenares.api.entity.enums.ModalidadEntrega;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PedidoResponseDTO {
    private Long id;
    private Long idVenta;
    private Long idUsuario;
    private String nombresUsuario;
    private String apellidosUsuario;
    private String emailUsuario;
    private Long idDistrito;
    private String nombreDistrito;
    private Long idAgenciaEnvio;
    private String nombreAgencia;
    private String direccionEnvio;
    private String referenciaCliente;
    private BigDecimal total;
    private LocalDateTime fechaRegistro;
    private LocalDateTime fechaEstimada;
    private LocalDateTime fechaEntrega;
    private EstadoPedido estado;
    private ModalidadEntrega modalidadEntrega;
}
