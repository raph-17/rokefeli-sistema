package com.rokefeli.colmenares.api.service;

import com.rokefeli.colmenares.api.entity.Pedido;
import com.rokefeli.colmenares.api.exception.ResourceNotFoundException;
import com.rokefeli.colmenares.api.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository repository;

    public PedidoServiceImpl(PedidoRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Pedido> findAll() {
        return repository.findAll();
    }

    @Override
    public Pedido findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido", id));
    }

    @Override
    public Pedido create(Pedido pedido) {
        return repository.save(pedido);
    }

    @Override
    public Pedido update(Long id, Pedido pedido) {
        Pedido existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido", id));
        // Simple replacement: preserve id
        pedido.setId(existing.getId());
        return repository.save(pedido);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Pedido", id);
        }
        repository.deleteById(id);
    }
}
