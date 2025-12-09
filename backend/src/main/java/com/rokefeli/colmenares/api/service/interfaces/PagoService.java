package com.rokefeli.colmenares.api.service.interfaces;

import com.rokefeli.colmenares.api.dto.create.PagoCreateDTO;
import com.rokefeli.colmenares.api.dto.response.PagoResponseDTO;

public interface PagoService {
    PagoResponseDTO procesarPago(PagoCreateDTO dto);
}


