package com.rokefeli.colmenares.api.service;

import java.util.List;
import com.rokefeli.colmenares.api.entity.Usuario;

public interface UsuarioService {
    List<Usuario> findAll();
    Usuario findById(Long id);
    Usuario create(Usuario usuario);
    Usuario update(Long id, Usuario usuario);
    void delete(Long id);
}
