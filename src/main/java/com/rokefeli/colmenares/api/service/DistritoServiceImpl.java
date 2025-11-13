package com.rokefeli.colmenares.api.service;

import com.rokefeli.colmenares.api.dto.create.DistritoCreateDTO;
import com.rokefeli.colmenares.api.dto.response.DistritoResponseDTO;
import com.rokefeli.colmenares.api.dto.update.DistritoUpdateDTO;
import com.rokefeli.colmenares.api.entity.Distrito;
import com.rokefeli.colmenares.api.exception.ResourceNotFoundException;
import com.rokefeli.colmenares.api.mapper.DistritoMapper;
import com.rokefeli.colmenares.api.repository.DistritoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DistritoServiceImpl implements DistritoService {

    @Autowired
    private DistritoRepository repository;

    @Autowired
    private DistritoMapper mapper;

    @Override
    public List<DistritoResponseDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Override
    public DistritoResponseDTO findById(Long id) {
        Distrito existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Distrito", id));
        return mapper.toResponseDTO(existing);
    }

    @Override
    public DistritoResponseDTO create(DistritoCreateDTO createDTO) {
        Distrito distrito = mapper.toEntity(createDTO);
        Distrito saved = repository.save(distrito);
        return mapper.toResponseDTO(saved);
    }

    @Override
    public DistritoResponseDTO update(Long id, DistritoUpdateDTO updateDTO) {
        Distrito existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Distrito", id));
        mapper.updateEntityFromDTO(updateDTO, existing);
        Distrito updated = repository.save(existing);
        return mapper.toResponseDTO(updated);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Distrito", id);
        }
        repository.deleteById(id);
    }
}
