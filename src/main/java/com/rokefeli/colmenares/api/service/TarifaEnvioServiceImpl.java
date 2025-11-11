package com.rokefeli.colmenares.api.service;

import com.rokefeli.colmenares.api.entity.TarifaEnvio;
import com.rokefeli.colmenares.api.exception.ResourceNotFoundException;
import com.rokefeli.colmenares.api.repository.TarifaEnvioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TarifaEnvioServiceImpl implements TarifaEnvioService {

    private final TarifaEnvioRepository repository;

    public TarifaEnvioServiceImpl(TarifaEnvioRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<TarifaEnvio> findAll() {
        return repository.findAll();
    }

    @Override
    public TarifaEnvio findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TarifaEnvio", id));
    }

    @Override
    public TarifaEnvio create(TarifaEnvio tarifaenvio) {
        return repository.save(tarifaenvio);
    }

    @Override
    public TarifaEnvio update(Long id, TarifaEnvio tarifaenvio) {
        TarifaEnvio existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TarifaEnvio", id));
        // Simple replacement: preserve id
        tarifaenvio.setId(existing.getId());
        return repository.save(tarifaenvio);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("TarifaEnvio", id);
        }
        repository.deleteById(id);
    }
}
