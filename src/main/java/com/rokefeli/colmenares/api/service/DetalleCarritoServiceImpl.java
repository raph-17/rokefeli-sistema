package com.rokefeli.colmenares.api.service;

import com.rokefeli.colmenares.api.entity.DetalleCarrito;
import com.rokefeli.colmenares.exception.ResourceNotFoundException;
import com.rokefeli.colmenares.api.repository.DetalleCarritoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DetalleCarritoServiceImpl implements DetalleCarritoService {

    private final DetalleCarritoRepository repository;

    public DetalleCarritoServiceImpl(DetalleCarritoRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<DetalleCarrito> findAll() {
        return repository.findAll();
    }

    @Override
    public DetalleCarrito findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DetalleCarrito", id));
    }

    @Override
    public DetalleCarrito create(DetalleCarrito detallecarrito) {
        return repository.save(detallecarrito);
    }

    @Override
    public DetalleCarrito update(Long id, DetalleCarrito detallecarrito) {
        DetalleCarrito existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DetalleCarrito", id));
        // Simple replacement: preserve id
        detallecarrito.setId(existing.getId());
        return repository.save(detallecarrito);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("DetalleCarrito", id);
        }
        repository.deleteById(id);
    }
}
