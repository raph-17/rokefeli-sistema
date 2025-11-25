package com.rokefeli.colmenares.api.dto.create;

import com.rokefeli.colmenares.api.entity.enums.MetodoPago;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PagoCreateDTO {

    @NotNull
    private Long idVenta;

    @NotNull
    private Long idTarifaEnvio;

    @NotNull
    private MetodoPago metodoPago;

    @NotNull
    @Min(value = 1)
    private BigDecimal monto;

    @NotNull
    private String tokenCulqi;

    @NotBlank
    @Email
    private String emailCliente;
}
