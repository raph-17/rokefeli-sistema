package com.rokefeli.colmenares.api.service.impl;

import com.rokefeli.colmenares.api.dto.create.AgenciaEnvioCreateDTO;
import com.rokefeli.colmenares.api.dto.response.AgenciaEnvioResponseDTO;
import com.rokefeli.colmenares.api.dto.update.AgenciaEnvioUpdateDTO;
import com.rokefeli.colmenares.api.entity.AgenciaEnvio;
import com.rokefeli.colmenares.api.entity.enums.EstadoAgencia;
import com.rokefeli.colmenares.api.exception.ResourceNotFoundException;
import com.rokefeli.colmenares.api.mapper.AgenciaEnvioMapper;
import com.rokefeli.colmenares.api.repository.AgenciaEnvioRepository;
import com.rokefeli.colmenares.api.service.interfaces.AgenciaEnvioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
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
    public List<AgenciaEnvioResponseDTO> findByEstado(EstadoAgencia estado) {
        return repository.findByEstado(estado)
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
    @Transactional
    public AgenciaEnvioResponseDTO create(AgenciaEnvioCreateDTO createDTO) {
        if(repository.existsByNombre(createDTO.getNombre())) {
            throw new IllegalArgumentException("Ya existe una agencia de envío con el nombre: " + createDTO.getNombre());
        }
        AgenciaEnvio agenciaEnvio = mapper.toEntity(createDTO);
        agenciaEnvio.setEstado(EstadoAgencia.ACTIVO);
        AgenciaEnvio saved = repository.save(agenciaEnvio);
        return mapper.toResponseDTO(saved);
    }

    @Override
    @Transactional
    public AgenciaEnvioResponseDTO update(Long id, AgenciaEnvioUpdateDTO updateDTO) {
        AgenciaEnvio existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AgenciaEnvio", id));
        mapper.updateEntityFromDTO(updateDTO, existing);
        AgenciaEnvio updated = repository.save(existing);
        return mapper.toResponseDTO(updated);
    }

    @Override
    @Transactional
    public void desactivar(Long id) {
        AgenciaEnvio existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AgenciaEnvio", id));
        existing.setEstado(EstadoAgencia.INACTIVO);
        repository.save(existing);
    }

    @Override
    @Transactional
    public void activar(Long id) {
        AgenciaEnvio existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AgenciaEnvio", id));
        existing.setEstado(EstadoAgencia.ACTIVO);
        repository.save(existing);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria", id));

        repository.deleteById(id);
    }
}
