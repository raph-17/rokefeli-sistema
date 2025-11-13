package com.rokefeli.colmenares.api.service;

import com.rokefeli.colmenares.api.dto.create.ProvinciaCreateDTO;
import com.rokefeli.colmenares.api.dto.response.ProvinciaResponseDTO;
import com.rokefeli.colmenares.api.dto.update.ProvinciaUpdateDTO;
import com.rokefeli.colmenares.api.entity.Provincia;
import com.rokefeli.colmenares.api.exception.ResourceNotFoundException;
import com.rokefeli.colmenares.api.mapper.ProvinciaMapper;
import com.rokefeli.colmenares.api.repository.ProvinciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProvinciaServiceImpl implements ProvinciaService {

    @Autowired
    private ProvinciaRepository repository;

    @Autowired
    private ProvinciaMapper mapper;

    @Override
    public List<ProvinciaResponseDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Override
    public ProvinciaResponseDTO findById(Long id) {
        Provincia existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Provincia", id));
        return mapper.toResponseDTO(existing);
    }

    @Override
    public ProvinciaResponseDTO create(ProvinciaCreateDTO createDTO) {
        Provincia provincia = mapper.toEntity(createDTO);
        Provincia saved = repository.save(provincia);
        return mapper.toResponseDTO(saved);
    }

    @Override
    public ProvinciaResponseDTO update(Long id, ProvinciaUpdateDTO updateDTO) {
        Provincia existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Provincia", id));
        mapper.updateEntityFromDTO(updateDTO, existing);
        Provincia updated = repository.save(existing);
        return mapper.toResponseDTO(updated);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Provincia", id);
        }
        repository.deleteById(id);
    }
}
