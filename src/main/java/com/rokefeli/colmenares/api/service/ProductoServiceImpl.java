package com.rokefeli.colmenares.api.service;

import com.rokefeli.colmenares.api.entity.Producto;
import com.rokefeli.colmenares.api.exception.ResourceNotFoundException;
import com.rokefeli.colmenares.api.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository repository;

    public ProductoServiceImpl(ProductoRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Producto> findAll() {
        return repository.findAll();
    }

    @Override
    public Producto findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", id));
    }

    @Override
    public Producto create(Producto producto) {
        return repository.save(producto);
    }

    @Override
    public Producto update(Long id, Producto producto) {
        Producto existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", id));
        // Simple replacement: preserve id
        producto.setId(existing.getId());
        return repository.save(producto);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Producto", id);
        }
        repository.deleteById(id);
    }
}
