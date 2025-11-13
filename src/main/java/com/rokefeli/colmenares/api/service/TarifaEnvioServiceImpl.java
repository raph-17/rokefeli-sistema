package com.rokefeli.colmenares.api.service;

import com.rokefeli.colmenares.api.dto.create.TarifaEnvioCreateDTO;
import com.rokefeli.colmenares.api.dto.response.TarifaEnvioResponseDTO;
import com.rokefeli.colmenares.api.dto.update.TarifaEnvioUpdateDTO;
import com.rokefeli.colmenares.api.entity.TarifaEnvio;
import com.rokefeli.colmenares.api.exception.ResourceNotFoundException;
import com.rokefeli.colmenares.api.mapper.TarifaEnvioMapper;
import com.rokefeli.colmenares.api.repository.TarifaEnvioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TarifaEnvioServiceImpl implements TarifaEnvioService {

    @Autowired
    private TarifaEnvioRepository repository;

    @Autowired
    private TarifaEnvioMapper mapper;

    @Override
    public List<TarifaEnvioResponseDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Override
    public TarifaEnvioResponseDTO findById(Long id) {
        TarifaEnvio existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TarifaEnvio", id));
        return mapper.toResponseDTO(existing);
    }

    @Override
    public TarifaEnvioResponseDTO create(TarifaEnvioCreateDTO createDTO) {
        TarifaEnvio tarifaenvio = mapper.toEntity(createDTO);
        TarifaEnvio saved = repository.save(tarifaenvio);
        return mapper.toResponseDTO(saved);
    }

    @Override
    public TarifaEnvioResponseDTO update(Long id, TarifaEnvioUpdateDTO updateDTO) {
        TarifaEnvio existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TarifaEnvio", id));
        mapper.updateEntityFromDTO(updateDTO, existing);
        TarifaEnvio updated = repository.save(existing);
        return mapper.toResponseDTO(updated);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("TarifaEnvio", id);
        }
        repository.deleteById(id);
    }
}
