package com.rokefeli.colmenares.api.service;

import com.rokefeli.colmenares.api.entity.Departamento;
import com.rokefeli.colmenares.api.exception.ResourceNotFoundException;
import com.rokefeli.colmenares.api.repository.DepartamentoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DepartamentoServiceImpl implements DepartamentoService {

    private final DepartamentoRepository repository;

    public DepartamentoServiceImpl(DepartamentoRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Departamento> findAll() {
        return repository.findAll();
    }

    @Override
    public Departamento findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Departamento", id));
    }

    @Override
    public Departamento create(Departamento departamento) {
        return repository.save(departamento);
    }

    @Override
    public Departamento update(Long id, Departamento departamento) {
        Departamento existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Departamento", id));
        // Simple replacement: preserve id
        departamento.setId(existing.getId());
        return repository.save(departamento);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Departamento", id);
        }
        repository.deleteById(id);
    }
}
