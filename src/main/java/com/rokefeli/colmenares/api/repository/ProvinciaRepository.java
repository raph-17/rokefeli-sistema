package com.rokefeli.colmenares.api.repository;

import com.rokefeli.colmenares.api.entity.Provincia;
import com.rokefeli.colmenares.api.entity.enums.EstadoProvincia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProvinciaRepository extends JpaRepository<Provincia, Long> {
    List<Provincia> findByDepartamento_Id(Long departamentoId);

    Optional<Provincia> findByIdAndEstado(Long id, EstadoProvincia estado);

    boolean existsByDepartamento_Id(Long departamentoId);

    boolean existsByNombreIgnoreCaseAndDepartamento_Id(String nombre, Long departamentoId);

    boolean existsByNombreIgnoreCaseAndDepartamento_IdAndIdNot(String nombre, Long departamentoId, Long id);

    @Modifying
    @Query("UPDATE Provincia p SET p.estado = :estado WHERE p.departamento.id = :idDepto")
    void actualizarEstadoPorDepartamento(Long idDepto, EstadoProvincia estado);
}
