package com.luxStock.demo.repository;

import com.luxStock.demo.model.entity.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmpleadoRepository extends JpaRepository<Empleado, Integer> {
    boolean existsByDocumento(String documento);
    Optional<Empleado> findByDocumento(String documento);

    Empleado findByIdEmpleado(Integer idEmpleado);
}
