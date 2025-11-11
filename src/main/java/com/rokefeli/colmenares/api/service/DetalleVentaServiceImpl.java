package com.rokefeli.colmenares.api.service;

import com.rokefeli.colmenares.api.entity.DetalleVenta;
import com.rokefeli.colmenares.exception.ResourceNotFoundException;
import com.rokefeli.colmenares.api.repository.DetalleVentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DetalleVentaServiceImpl implements DetalleVentaService {

    private final DetalleVentaRepository repository;

    public DetalleVentaServiceImpl(DetalleVentaRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<DetalleVenta> findAll() {
        return repository.findAll();
    }

    @Override
    public DetalleVenta findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DetalleVenta", id));
    }

    @Override
    public DetalleVenta create(DetalleVenta detalleventa) {
        return repository.save(detalleventa);
    }

    @Override
    public DetalleVenta update(Long id, DetalleVenta detalleventa) {
        DetalleVenta existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DetalleVenta", id));
        // Simple replacement: preserve id
        detalleventa.setId(existing.getId());
        return repository.save(detalleventa);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("DetalleVenta", id);
        }
        repository.deleteById(id);
    }
}
