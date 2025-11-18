package com.rokefeli.colmenares.api.service.impl;

import com.rokefeli.colmenares.api.dto.create.AdminCreateDTO;
import com.rokefeli.colmenares.api.dto.create.UsuarioCreateDTO;
import com.rokefeli.colmenares.api.dto.response.UsuarioResponseDTO;
import com.rokefeli.colmenares.api.dto.update.AdminUpdateDTO;
import com.rokefeli.colmenares.api.dto.update.PasswordChangeDTO;
import com.rokefeli.colmenares.api.dto.update.UsuarioUpdateDTO;
import com.rokefeli.colmenares.api.entity.Usuario;
import com.rokefeli.colmenares.api.entity.enums.EstadoUsuario;
import com.rokefeli.colmenares.api.entity.enums.Rol;
import com.rokefeli.colmenares.api.exception.ResourceNotFoundException;
import com.rokefeli.colmenares.api.mapper.UsuarioMapper;
import com.rokefeli.colmenares.api.repository.UsuarioRepository;
import com.rokefeli.colmenares.api.repository.VentaRepository;
import com.rokefeli.colmenares.api.service.interfaces.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private UsuarioMapper usuarioMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UsuarioResponseDTO findById(Long id) {
        Usuario user = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", id));
        return usuarioMapper.toResponseDTO(user);
    }

    @Override
    public List<UsuarioResponseDTO> findAll() {
        return usuarioRepository.findAll()
                .stream()
                .map(usuarioMapper::toResponseDTO)
                .toList();
    }

    @Override
    public List<UsuarioResponseDTO> findByEstado(EstadoUsuario estado) {
        return usuarioRepository.findByEstado(estado)
                .stream()
                .map(usuarioMapper::toResponseDTO)
                .toList();
    }

    @Override
    public UsuarioResponseDTO findByDni(String dni) {
        Usuario user = usuarioRepository.findByDni(dni)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario"));
        return usuarioMapper.toResponseDTO(user);
    }

    @Override
    public UsuarioResponseDTO registrarCliente(UsuarioCreateDTO dto) {

        validarEmailYdni(dto.getEmail(), dto.getDni());

        Usuario usuario = usuarioMapper.toEntity(dto);
        usuario.setRol(Rol.CLIENTE);
        usuario.setEstado(EstadoUsuario.ACTIVO);
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));

        return usuarioMapper.toResponseDTO(usuarioRepository.save(usuario));
    }

    @Override
    public UsuarioResponseDTO registrarAdmin(AdminCreateDTO dto) {

        validarEmailYdni(dto.getEmail(), dto.getDni());

        Usuario usuario = usuarioMapper.toEntity(dto);
        usuario.setEstado(EstadoUsuario.ACTIVO);
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));

        return usuarioMapper.toResponseDTO(usuarioRepository.save(usuario));
    }

    @Override
    public UsuarioResponseDTO updateUsuario(Long id, UsuarioUpdateDTO dto) {

        Usuario existing = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", id));

        usuarioMapper.updateEntityFromDTO(dto, existing);
        return usuarioMapper.toResponseDTO(usuarioRepository.save(existing));
    }

    @Override
    public UsuarioResponseDTO updateAdmin(Long id, AdminUpdateDTO dto) {

        Usuario existing = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", id));

        usuarioMapper.updateEntityFromDTO(dto, existing);
        return usuarioMapper.toResponseDTO(usuarioRepository.save(existing));
    }

    @Override
    public void cambiarPassword(Long id, PasswordChangeDTO dto) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", id));

        if(passwordEncoder.encode(dto.getActualPassword()).equals(usuario.getPassword())) {
            usuario.setPassword(passwordEncoder.encode(dto.getNuevaPassword()));
            usuarioRepository.save(usuario);
        } else {
            throw new IllegalArgumentException("La contraseña ingresada no es correcta");
        }
    }

    @Override
    public void cambiarEstado(Long id, EstadoUsuario nuevoEstado) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", id));

        usuario.setEstado(nuevoEstado);
        usuarioRepository.save(usuario);
    }

    @Override
    public void delete(Long id) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", id));

        if (ventaRepository.existsByUsuario_Id(id)) {
            throw new IllegalStateException("No se puede eliminar un usuario con ventas registradas.");
        }

        usuarioRepository.delete(usuario);
    }

    private void validarEmailYdni(String email, String dni) {

        if (usuarioRepository.existsByEmailIgnoreCase(email)) {
            throw new IllegalArgumentException("El email ya está registrado.");
        }

        if (usuarioRepository.existsByDni(dni)) {
            throw new IllegalArgumentException("El DNI ya está registrado.");
        }
    }
}

