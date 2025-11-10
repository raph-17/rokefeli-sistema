package com.rokefeli.colmenares.api.repository;

import com.rokefeli.colmenares.api.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
}
