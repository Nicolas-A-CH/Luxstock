package com.luxStock.demo.repository;

import com.luxStock.demo.model.entity.Empleado;
import com.luxStock.demo.model.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);
    Optional<Usuario> findByEmpleado(Empleado empleado);
}
