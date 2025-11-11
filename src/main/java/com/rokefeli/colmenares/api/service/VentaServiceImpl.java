package com.rokefeli.colmenares.api.service;

import com.rokefeli.colmenares.api.entity.Venta;
import com.rokefeli.colmenares.exception.ResourceNotFoundException;
import com.rokefeli.colmenares.api.repository.VentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class VentaServiceImpl implements VentaService {

    private final VentaRepository repository;

    public VentaServiceImpl(VentaRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Venta> findAll() {
        return repository.findAll();
    }

    @Override
    public Venta findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venta", id));
    }

    @Override
    public Venta create(Venta venta) {
        return repository.save(venta);
    }

    @Override
    public Venta update(Long id, Venta venta) {
        Venta existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venta", id));
        // Simple replacement: preserve id
        venta.setId(existing.getId());
        return repository.save(venta);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Venta", id);
        }
        repository.deleteById(id);
    }
}
