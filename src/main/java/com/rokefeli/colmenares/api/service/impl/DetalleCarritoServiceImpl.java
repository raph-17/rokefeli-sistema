package com.rokefeli.colmenares.api.service.impl;

import com.rokefeli.colmenares.api.dto.create.DetalleCarritoCreateDTO;
import com.rokefeli.colmenares.api.dto.response.DetalleCarritoResponseDTO;
import com.rokefeli.colmenares.api.entity.DetalleCarrito;
import com.rokefeli.colmenares.api.exception.ResourceNotFoundException;
import com.rokefeli.colmenares.api.mapper.DetalleCarritoMapper;
import com.rokefeli.colmenares.api.repository.DetalleCarritoRepository;
import com.rokefeli.colmenares.api.service.interfaces.DetalleCarritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DetalleCarritoServiceImpl implements DetalleCarritoService {

    @Autowired
    private DetalleCarritoRepository repository;

    @Autowired
    private DetalleCarritoMapper mapper;

    @Override
    public List<DetalleCarritoResponseDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Override
    public DetalleCarritoResponseDTO findById(Long id) {
        DetalleCarrito existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DetalleCarrito", id));
        return mapper.toResponseDTO(existing);
    }

    @Override
    public DetalleCarritoResponseDTO create(DetalleCarritoCreateDTO createDTO) {
        DetalleCarrito detallecarrito = mapper.toEntity(createDTO);
        DetalleCarrito saved = repository.save(detallecarrito);
        return mapper.toResponseDTO(saved);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("DetalleCarrito", id);
        }
        repository.deleteById(id);
    }
}
