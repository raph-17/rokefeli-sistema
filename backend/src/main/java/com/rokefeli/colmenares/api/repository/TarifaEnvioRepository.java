package com.rokefeli.colmenares.api.repository;

import com.rokefeli.colmenares.api.entity.TarifaEnvio;
import com.rokefeli.colmenares.api.entity.enums.EstadoTarifa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TarifaEnvioRepository extends JpaRepository<TarifaEnvio, Long> {
    List<TarifaEnvio> findByAgenciaEnvio_Id(Long agenciaEnvioId);
    List<TarifaEnvio> findByEstado(EstadoTarifa estado);
    List<TarifaEnvio> findByDistrito_Id(Long idDistrito);
    List<TarifaEnvio> findByDistrito_IdAndEstado(Long idDistrito, EstadoTarifa estado);
    Optional<TarifaEnvio> findByIdAndEstado(Long id, EstadoTarifa estado);
    Optional<TarifaEnvio> findByAgenciaEnvio_IdAndDistrito_IdAndEstado(Long idAgencia, Long idDistrito, EstadoTarifa estado);
    Optional<TarifaEnvio> findByAgenciaEnvio_IdAndDistrito_Id(Long idAgencia, Long idDistrito);

    @Modifying
    @Query("UPDATE TarifaEnvio t SET t.estado = :estado WHERE t.distrito.provincia.departamento.id = :idDepto")
    void actualizarEstadoPorDepartamento(Long idDepto, EstadoTarifa estado);

    @Modifying
    @Query("UPDATE TarifaEnvio t SET t.estado = :estado WHERE t.distrito.provincia.id = :idProv")
    void actualizarEstadoPorProvincia(Long idProv, EstadoTarifa estado);

    @Modifying
    @Query("UPDATE TarifaEnvio t SET t.estado = :estado WHERE t.distrito.id = :idDistrito")
    void actualizarEstadoPorDistrito(Long idDistrito, EstadoTarifa estado);

    @Modifying
    @Query("UPDATE TarifaEnvio t SET t.estado = :estado WHERE t.agenciaEnvio.id = :idAgenciaEnvio")
    void actualizarEstadoPorAgencia(Long idAgenciaEnvio, EstadoTarifa estado);
}
