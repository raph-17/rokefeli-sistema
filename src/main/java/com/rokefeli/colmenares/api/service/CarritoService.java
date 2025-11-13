package com.rokefeli.colmenares.api.service;

import java.util.List;

import com.rokefeli.colmenares.api.dto.response.CarritoResponseDTO;

public interface CarritoService {
    List<CarritoResponseDTO> findAll();
    CarritoResponseDTO findById(Long id);
    void hardDelete(Long id);
}
