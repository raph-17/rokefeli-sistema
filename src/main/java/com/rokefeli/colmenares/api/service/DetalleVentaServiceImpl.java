package com.rokefeli.colmenares.api.service;

import com.rokefeli.colmenares.api.dto.create.DetalleVentaCreateDTO;
import com.rokefeli.colmenares.api.dto.response.DetalleVentaResponseDTO;
import com.rokefeli.colmenares.api.entity.DetalleVenta;
import com.rokefeli.colmenares.api.exception.ResourceNotFoundException;
import com.rokefeli.colmenares.api.mapper.DetalleVentaMapper;
import com.rokefeli.colmenares.api.repository.DetalleVentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DetalleVentaServiceImpl implements DetalleVentaService {

    @Autowired
    private DetalleVentaRepository repository;

    @Autowired
    private DetalleVentaMapper mapper;

    @Override
    public List<DetalleVentaResponseDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Override
    public DetalleVentaResponseDTO findById(Long id) {
        DetalleVenta existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DetalleVenta", id));
        return mapper.toResponseDTO(existing);
    }

    @Override
    public DetalleVentaResponseDTO create(DetalleVentaCreateDTO createDTO) {
        DetalleVenta detalleventa = mapper.toEntity(createDTO);
        DetalleVenta saved = repository.save(detalleventa);
        return mapper.toResponseDTO(saved);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("DetalleVenta", id);
        }
        repository.deleteById(id);
    }
}
