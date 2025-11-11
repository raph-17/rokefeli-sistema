package com.rokefeli.colmenares.api.service;

import com.rokefeli.colmenares.api.entity.Categoria;
import com.rokefeli.colmenares.api.exception.ResourceNotFoundException;
import com.rokefeli.colmenares.api.repository.CategoriaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository repository;

    public CategoriaServiceImpl(CategoriaRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Categoria> findAll() {
        return repository.findAll();
    }

    @Override
    public Categoria findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria", id));
    }

    @Override
    public Categoria create(Categoria categoria) {
        return repository.save(categoria);
    }

    @Override
    public Categoria update(Long id, Categoria categoria) {
        Categoria existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria", id));
        // Simple replacement: preserve id
        categoria.setId(existing.getId());
        return repository.save(categoria);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Categoria", id);
        }
        repository.deleteById(id);
    }
}
