package com.rokefeli.colmenares.api.service;

import com.rokefeli.colmenares.api.dto.create.UsuarioCreateDTO;
import com.rokefeli.colmenares.api.dto.response.UsuarioResponseDTO;
import com.rokefeli.colmenares.api.dto.update.UsuarioUpdateDTO;
import com.rokefeli.colmenares.api.entity.Usuario;
import com.rokefeli.colmenares.api.exception.ResourceNotFoundException;
import com.rokefeli.colmenares.api.mapper.UsuarioMapper;
import com.rokefeli.colmenares.api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private UsuarioMapper mapper;

    @Override
    public List<UsuarioResponseDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Override
    public UsuarioResponseDTO findById(Long id) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", id));
        return mapper.toResponseDTO(usuario);
    }

    @Override
    public UsuarioResponseDTO create(UsuarioCreateDTO createDTO) {
        Usuario usuario = mapper.toEntity(createDTO);
        Usuario saved = repository.save(usuario);
        return mapper.toResponseDTO(saved);
    }

    @Override
    public UsuarioResponseDTO update(Long id, UsuarioUpdateDTO updateDTO) {
        Usuario existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", id));
        mapper.updateEntityFromDTO(updateDTO, existing);
        Usuario updated = repository.save(existing);
        return mapper.toResponseDTO(updated);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Usuario", id);
        }
        repository.deleteById(id);
    }
}
