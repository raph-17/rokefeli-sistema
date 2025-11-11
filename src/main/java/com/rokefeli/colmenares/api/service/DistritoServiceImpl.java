package com.rokefeli.colmenares.api.service;

import com.rokefeli.colmenares.api.entity.Distrito;
import com.rokefeli.colmenares.exception.ResourceNotFoundException;
import com.rokefeli.colmenares.api.repository.DistritoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DistritoServiceImpl implements DistritoService {

    private final DistritoRepository repository;

    public DistritoServiceImpl(DistritoRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Distrito> findAll() {
        return repository.findAll();
    }

    @Override
    public Distrito findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Distrito", id));
    }

    @Override
    public Distrito create(Distrito distrito) {
        return repository.save(distrito);
    }

    @Override
    public Distrito update(Long id, Distrito distrito) {
        Distrito existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Distrito", id));
        // Simple replacement: preserve id
        distrito.setId(existing.getId());
        return repository.save(distrito);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Distrito", id);
        }
        repository.deleteById(id);
    }
}
