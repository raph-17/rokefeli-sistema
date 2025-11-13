package com.rokefeli.colmenares.api.service;

import com.rokefeli.colmenares.api.dto.create.DepartamentoCreateDTO;
import com.rokefeli.colmenares.api.dto.response.DepartamentoResponseDTO;
import com.rokefeli.colmenares.api.dto.update.DepartamentoUpdateDTO;
import com.rokefeli.colmenares.api.entity.Departamento;
import com.rokefeli.colmenares.api.exception.ResourceNotFoundException;
import com.rokefeli.colmenares.api.mapper.DepartamentoMapper;
import com.rokefeli.colmenares.api.repository.DepartamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DepartamentoServiceImpl implements DepartamentoService {

    @Autowired
    private DepartamentoRepository repository;

    @Autowired
    private DepartamentoMapper mapper;

    @Override
    public List<DepartamentoResponseDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Override
    public DepartamentoResponseDTO findById(Long id) {
        Departamento existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Departamento", id));
        return mapper.toResponseDTO(existing);
    }

    @Override
    public DepartamentoResponseDTO create(DepartamentoCreateDTO createDTO) {
        Departamento departamento = mapper.toEntity(createDTO);
        Departamento saved = repository.save(departamento);
        return mapper.toResponseDTO(saved);
    }

    @Override
    public DepartamentoResponseDTO update(Long id, DepartamentoUpdateDTO updateDTO) {
        Departamento existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Departamento", id));
        mapper.updateEntityFromDTO(updateDTO, existing);
        Departamento updated = repository.save(existing);
        return mapper.toResponseDTO(updated);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Departamento", id);
        }
        repository.deleteById(id);
    }
}
