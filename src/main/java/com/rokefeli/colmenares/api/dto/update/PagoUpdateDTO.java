package com.rokefeli.colmenares.api.dto.update;

import com.rokefeli.colmenares.api.entity.enums.EstadoPago;
import lombok.Data;

@Data
public class PagoUpdateDTO {
    private EstadoPago estadoPago;
}
