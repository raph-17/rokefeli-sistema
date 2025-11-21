package com.rokefeli.colmenares.api.repository;

import com.rokefeli.colmenares.api.entity.TarifaEnvio;
import com.rokefeli.colmenares.api.entity.enums.EstadoTarifa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TarifaEnvioRepository extends JpaRepository<TarifaEnvio, Long> {
    List<TarifaEnvio> findByAgenciaEnvio_Id(Long agenciaEnvioId);
    Optional<TarifaEnvio> findByIdAndEstado(Long id, EstadoTarifa estado);
    Optional<TarifaEnvio> findByAgenciaEnvio_IdAndDistrito_Id(Long idAgencia, Long idDistrito);
}
