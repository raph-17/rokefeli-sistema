package com.rokefeli.colmenares.api.security;

import com.rokefeli.colmenares.api.entity.Usuario;
import com.rokefeli.colmenares.api.entity.Venta;
import com.rokefeli.colmenares.api.repository.UsuarioRepository;
import com.rokefeli.colmenares.api.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class SecurityService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private VentaRepository ventaRepository;

    // Verifica que el usuario este logeado
    public boolean isSelf(Authentication auth, Long id) {
        JwtUserDetails user = (JwtUserDetails) auth.getPrincipal();
        return user.getId().equals(id);
    }

    // Verifica que el usuario autenticado es dueño de la venta
    public boolean isVentaOwner(Authentication authentication, Long idVenta) {

        // Si es admin, acceso completo
        if (authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return true;
        }

        // Buscar usuario autenticado por email
        String email = authentication.getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElse(null);

        if (usuario == null) {
            return false;
        }

        // Buscar venta
        Venta venta = ventaRepository.findById(idVenta)
                .orElse(null);

        if (venta == null) {
            return false;
        }

        // Comparar dueño de la venta con el usuario del token
        return venta.getUsuario().getId().equals(usuario.getId());
    }
}
