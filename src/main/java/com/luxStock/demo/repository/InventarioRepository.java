package com.luxStock.demo.repository;

import com.luxStock.demo.model.entity.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface InventarioRepository extends JpaRepository<Inventario, Integer> {
    @Query("SELECT i FROM Inventario i WHERE LOWER(i.producto.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<Inventario> findByProductoNombreContainingIgnoreCase(@Param("nombre") String nombre);
}