package com.rokefeli.colmenares.api.service.impl;

import com.rokefeli.colmenares.api.dto.create.PagoCreateDTO;
import com.rokefeli.colmenares.api.dto.response.PagoResponseDTO;
import com.rokefeli.colmenares.api.dto.update.PagoUpdateDTO;
import com.rokefeli.colmenares.api.entity.Pago;
import com.rokefeli.colmenares.api.exception.ResourceNotFoundException;
import com.rokefeli.colmenares.api.mapper.PagoMapper;
import com.rokefeli.colmenares.api.repository.PagoRepository;
import com.rokefeli.colmenares.api.service.interfaces.PagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PagoServiceImpl implements PagoService {

    @Autowired
    private PagoRepository repository;

    @Autowired
    private PagoMapper mapper;

    @Override
    public List<PagoResponseDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Override
    public PagoResponseDTO findById(Long id) {
        Pago existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pago", id));
        return mapper.toResponseDTO(existing);
    }

    @Override
    public PagoResponseDTO create(PagoCreateDTO createDTO) {
        Pago pago = mapper.toEntity(createDTO);
        Pago saved = repository.save(pago);
        return mapper.toResponseDTO(saved);
    }

    @Override
    public PagoResponseDTO update(Long id, PagoUpdateDTO updateDTO) {
        Pago existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pago", id));
        mapper.updateEntityFromDTO(updateDTO, existing);
        Pago updated = repository.save(existing);
        return mapper.toResponseDTO(updated);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Pago", id);
        }
        repository.deleteById(id);
    }
}
