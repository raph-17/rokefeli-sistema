package com.rokefeli.colmenares.api.service;

import com.rokefeli.colmenares.api.dto.response.CarritoResponseDTO;
import com.rokefeli.colmenares.api.entity.Carrito;
import com.rokefeli.colmenares.api.exception.ResourceNotFoundException;
import com.rokefeli.colmenares.api.mapper.CarritoMapper;
import com.rokefeli.colmenares.api.repository.CarritoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CarritoServiceImpl implements CarritoService {

    @Autowired
    private CarritoRepository repository;

    @Autowired
    private CarritoMapper mapper;

    @Override
    public List<CarritoResponseDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Override
    public CarritoResponseDTO findById(Long id) {
        Carrito existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Carrito", id));
        return mapper.toResponseDTO(existing);
    }

    @Override
    public void hardDelete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Carrito", id);
        }
        repository.deleteById(id);
    }
}
