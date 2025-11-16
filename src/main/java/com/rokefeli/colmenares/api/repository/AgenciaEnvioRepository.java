package com.rokefeli.colmenares.api.repository;

import com.rokefeli.colmenares.api.entity.AgenciaEnvio;
import com.rokefeli.colmenares.api.entity.enums.EstadoAgencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AgenciaEnvioRepository extends JpaRepository<AgenciaEnvio, Long> {
    List<AgenciaEnvio> findByEstado(EstadoAgencia estado);
    Optional<AgenciaEnvio> findByIdAndEstado(Long id, EstadoAgencia estado);
    boolean existsByNombre(String nombre);
}
