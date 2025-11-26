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

        // 1. Si no está autenticado, rechazar
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        // 2. Si es ADMIN, pase VIP
        // (Una forma más corta de verificar el rol)
        if (authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return true;
        }

        // 3. OPTIMIZACIÓN: Obtener ID del usuario directamente del Token (Sin ir a la BD)
        // Como tu filtro ya configuró el contexto con JwtUserDetails, hacemos un cast seguro
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof JwtUserDetails)) {
            return false; // Por si acaso es un usuario anónimo
        }
        Long userId = ((JwtUserDetails) principal).getId();

        // 4. Buscar solo la venta
        // Usamos map/orElse para hacerlo más funcional y limpio
        return ventaRepository.findById(idVenta)
                .map(venta -> venta.getUsuario().getId().equals(userId))
                .orElse(false); // Si no existe la venta, retorna false (403 Forbidden)
    }
}
