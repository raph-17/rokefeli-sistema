package com.rokefeli.colmenares.api.repository;

import com.rokefeli.colmenares.api.entity.Distrito;
import com.rokefeli.colmenares.api.entity.enums.EstadoDistrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DistritoRepository extends JpaRepository<Distrito, Long> {
    List<Distrito> findByProvincia_Id(Long provinciaId);
    List<Distrito> findByEstado(EstadoDistrito estado);
    Optional<Distrito> findByIdAndEstado(Long id, EstadoDistrito estado);
    boolean existsByNombreIgnoreCaseAndProvincia_Id(String nombre, Long provinciaId);
    boolean existsByProvincia_Id(Long provinciaId);
    boolean existsByNombreIgnoreCaseAndProvincia_IdAndIdNot(String nombre, Long provinciaId, Long id);

    @Modifying
    @Query("UPDATE Distrito d SET d.estado = :estado WHERE d.provincia.departamento.id = :idDepto")
    void actualizarEstadoPorDepartamento(Long idDepto, EstadoDistrito estado);

    @Modifying
    @Query("UPDATE Distrito d SET d.estado = :estado WHERE d.provincia.id = :idProv")
    void actualizarEstadoPorProvincia(Long idProv, EstadoDistrito estado);
}
