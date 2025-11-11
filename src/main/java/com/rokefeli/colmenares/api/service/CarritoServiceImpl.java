package com.rokefeli.colmenares.api.service;

import com.rokefeli.colmenares.api.entity.Carrito;
import com.rokefeli.colmenares.exception.ResourceNotFoundException;
import com.rokefeli.colmenares.api.repository.CarritoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CarritoServiceImpl implements CarritoService {

    private final CarritoRepository repository;

    public CarritoServiceImpl(CarritoRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Carrito> findAll() {
        return repository.findAll();
    }

    @Override
    public Carrito findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Carrito", id));
    }

    @Override
    public Carrito create(Carrito carrito) {
        return repository.save(carrito);
    }

    @Override
    public Carrito update(Long id, Carrito carrito) {
        Carrito existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Carrito", id));
        // Simple replacement: preserve id
        carrito.setId(existing.getId());
        return repository.save(carrito);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Carrito", id);
        }
        repository.deleteById(id);
    }
}
