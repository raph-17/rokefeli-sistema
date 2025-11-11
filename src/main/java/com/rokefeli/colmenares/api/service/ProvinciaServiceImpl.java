package com.rokefeli.colmenares.api.service;

import com.rokefeli.colmenares.api.entity.Provincia;
import com.rokefeli.colmenares.exception.ResourceNotFoundException;
import com.rokefeli.colmenares.api.repository.ProvinciaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProvinciaServiceImpl implements ProvinciaService {

    private final ProvinciaRepository repository;

    public ProvinciaServiceImpl(ProvinciaRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Provincia> findAll() {
        return repository.findAll();
    }

    @Override
    public Provincia findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Provincia", id));
    }

    @Override
    public Provincia create(Provincia provincia) {
        return repository.save(provincia);
    }

    @Override
    public Provincia update(Long id, Provincia provincia) {
        Provincia existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Provincia", id));
        // Simple replacement: preserve id
        provincia.setId(existing.getId());
        return repository.save(provincia);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Provincia", id);
        }
        repository.deleteById(id);
    }
}
