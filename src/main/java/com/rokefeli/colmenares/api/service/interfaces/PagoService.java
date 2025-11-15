package com.rokefeli.colmenares.api.service.interfaces;

import java.util.List;

import com.rokefeli.colmenares.api.dto.create.PagoCreateDTO;
import com.rokefeli.colmenares.api.dto.response.PagoResponseDTO;
import com.rokefeli.colmenares.api.dto.update.PagoUpdateDTO;

public interface PagoService {
    PagoResponseDTO procesarPago(PagoCreateDTO dto);
}


