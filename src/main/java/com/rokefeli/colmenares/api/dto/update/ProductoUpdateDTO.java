package com.rokefeli.colmenares.api.dto.update;

import com.rokefeli.colmenares.api.dto.create.ProductoCreateDTO;
import com.rokefeli.colmenares.api.entity.enums.EstadoProducto;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductoUpdateDTO extends ProductoCreateDTO {
    private EstadoProducto estado;
}
