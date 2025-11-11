package com.rokefeli.colmenares.api.service;

import com.rokefeli.colmenares.api.entity.Pago;
import com.rokefeli.colmenares.api.exception.ResourceNotFoundException;
import com.rokefeli.colmenares.api.repository.PagoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PagoServiceImpl implements PagoService {

    private final PagoRepository repository;

    public PagoServiceImpl(PagoRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Pago> findAll() {
        return repository.findAll();
    }

    @Override
    public Pago findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pago", id));
    }

    @Override
    public Pago create(Pago pago) {
        return repository.save(pago);
    }

    @Override
    public Pago update(Long id, Pago pago) {
        Pago existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pago", id));
        // Simple replacement: preserve id
        pago.setId(existing.getId());
        return repository.save(pago);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Pago", id);
        }
        repository.deleteById(id);
    }
}
