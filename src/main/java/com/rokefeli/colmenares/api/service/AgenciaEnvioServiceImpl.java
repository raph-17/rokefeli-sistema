package com.rokefeli.colmenares.api.service;

import com.rokefeli.colmenares.api.dto.create.AgenciaEnvioCreateDTO;
import com.rokefeli.colmenares.api.dto.response.AgenciaEnvioResponseDTO;
import com.rokefeli.colmenares.api.dto.update.AgenciaEnvioUpdateDTO;
import com.rokefeli.colmenares.api.entity.AgenciaEnvio;
import com.rokefeli.colmenares.api.entity.enums.EstadoAgencia;
import com.rokefeli.colmenares.api.exception.ResourceNotFoundException;
import com.rokefeli.colmenares.api.mapper.AgenciaEnvioMapper;
import com.rokefeli.colmenares.api.repository.AgenciaEnvioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AgenciaEnvioServiceImpl implements AgenciaEnvioService {

    @Autowired
    private AgenciaEnvioRepository repository;

    @Autowired
    private AgenciaEnvioMapper mapper;

    @Override
    public List<AgenciaEnvioResponseDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Override
    public AgenciaEnvioResponseDTO findById(Long id) {
        AgenciaEnvio agenciaEnvio = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AgenciaEnvio", id));
        return mapper.toResponseDTO(agenciaEnvio);
    }

    @Override
    public AgenciaEnvioResponseDTO create(AgenciaEnvioCreateDTO createDTO) {
        AgenciaEnvio agenciaEnvio = mapper.toEntity(createDTO);
        AgenciaEnvio saved = repository.save(agenciaEnvio);
        return mapper.toResponseDTO(saved);
    }

    @Override
    public AgenciaEnvioResponseDTO update(Long id, AgenciaEnvioUpdateDTO updateDTO) {
        AgenciaEnvio existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AgenciaEnvio", id));
        mapper.updateEntityFromDTO(updateDTO, existing);
        AgenciaEnvio updated = repository.save(existing);
        return mapper.toResponseDTO(updated);
    }

    @Override
    public void softDelete(Long id) {
        AgenciaEnvio existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AgenciaEnvio", id));
        existing.setEstado(EstadoAgencia.INACTIVO);
        repository.save(existing);
    }

    @Override
    public void hardDelete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("AgenciaEnvio", id);
        }
        repository.deleteById(id);
    }
}
