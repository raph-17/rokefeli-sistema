package com.rokefeli.colmenares.api.service;

import java.util.List;

import com.rokefeli.colmenares.api.dto.create.PagoCreateDTO;
import com.rokefeli.colmenares.api.dto.response.PagoResponseDTO;
import com.rokefeli.colmenares.api.dto.update.PagoUpdateDTO;
import com.rokefeli.colmenares.api.entity.Pago;

public interface PagoService {
    List<PagoResponseDTO> findAll();
    PagoResponseDTO findById(Long id);
    PagoResponseDTO create(PagoCreateDTO createDTO);
    PagoResponseDTO update(Long id, PagoUpdateDTO updateDTO);
    void delete(Long id);
}
