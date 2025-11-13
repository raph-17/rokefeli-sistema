package com.rokefeli.colmenares.api.service;

import com.rokefeli.colmenares.api.dto.create.VentaCreateDTO;
import com.rokefeli.colmenares.api.dto.response.VentaResponseDTO;
import com.rokefeli.colmenares.api.entity.Venta;
import com.rokefeli.colmenares.api.exception.ResourceNotFoundException;
import com.rokefeli.colmenares.api.mapper.VentaMapper;
import com.rokefeli.colmenares.api.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class VentaServiceImpl implements VentaService {

    @Autowired
    private VentaRepository repository;

    @Autowired
    private VentaMapper mapper;

    @Override
    public List<VentaResponseDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Override
    public VentaResponseDTO findById(Long id) {
        Venta venta = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venta", id));
        return mapper.toResponseDTO(venta);
    }

    @Override
    public VentaResponseDTO create(VentaCreateDTO createDTO) {
        Venta venta = mapper.toEntity(createDTO);
        Venta saved = repository.save(venta);
        return mapper.toResponseDTO(saved);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Venta", id);
        }
        repository.deleteById(id);
    }
}
