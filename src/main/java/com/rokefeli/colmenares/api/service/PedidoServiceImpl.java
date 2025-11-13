package com.rokefeli.colmenares.api.service;

import com.rokefeli.colmenares.api.dto.create.PedidoCreateDTO;
import com.rokefeli.colmenares.api.dto.response.PedidoResponseDTO;
import com.rokefeli.colmenares.api.dto.update.PedidoUpdateDTO;
import com.rokefeli.colmenares.api.entity.Pedido;
import com.rokefeli.colmenares.api.exception.ResourceNotFoundException;
import com.rokefeli.colmenares.api.mapper.PedidoMapper;
import com.rokefeli.colmenares.api.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PedidoServiceImpl implements PedidoService {

    @Autowired
    private PedidoRepository repository;

    @Autowired
    private PedidoMapper mapper;

    @Override
    public List<PedidoResponseDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Override
    public PedidoResponseDTO findById(Long id) {
        Pedido existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido", id));
        return mapper.toResponseDTO(existing);
    }

    @Override
    public PedidoResponseDTO create(PedidoCreateDTO createDTO) {
        Pedido pedido = mapper.toEntity(createDTO);
        Pedido saved = repository.save(pedido);
        return mapper.toResponseDTO(saved);
    }

    @Override
    public PedidoResponseDTO update(Long id, PedidoUpdateDTO updateDTO) {
        Pedido existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido", id));
        mapper.updateEntityFromDTO(updateDTO, existing);
        Pedido updated = repository.save(existing);
        return mapper.toResponseDTO(updated);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Pedido", id);
        }
        repository.deleteById(id);
    }
}
