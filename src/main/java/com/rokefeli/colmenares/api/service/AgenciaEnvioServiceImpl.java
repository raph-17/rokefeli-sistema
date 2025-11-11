package com.rokefeli.colmenares.api.service;

import com.rokefeli.colmenares.api.entity.AgenciaEnvio;
import com.rokefeli.colmenares.api.exception.ResourceNotFoundException;
import com.rokefeli.colmenares.api.repository.AgenciaEnvioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AgenciaEnvioServiceImpl implements AgenciaEnvioService {

    private final AgenciaEnvioRepository repository;

    public AgenciaEnvioServiceImpl(AgenciaEnvioRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<AgenciaEnvio> findAll() {
        return repository.findAll();
    }

    @Override
    public AgenciaEnvio findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AgenciaEnvio", id));
    }

    @Override
    public AgenciaEnvio create(AgenciaEnvio agenciaenvio) {
        return repository.save(agenciaenvio);
    }

    @Override
    public AgenciaEnvio update(Long id, AgenciaEnvio agenciaenvio) {
        AgenciaEnvio existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AgenciaEnvio", id));
        // Simple replacement: preserve id
        agenciaenvio.setId(existing.getId());
        return repository.save(agenciaenvio);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("AgenciaEnvio", id);
        }
        repository.deleteById(id);
    }
}
