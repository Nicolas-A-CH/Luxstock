package com.luxStock.demo.repository;

import com.luxStock.demo.model.entity.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional; // Importante para manejar valores nulos de forma segura

public interface InventarioRepository extends JpaRepository<Inventario, Integer> {

    // Método que ya tenías (puedes conservarlo si lo usas en otros lados)
    @Query("SELECT i.cantidad FROM Inventario i WHERE LOWER(i.producto.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<Inventario> findByProductoNombreContainingIgnoreCase(@Param("nombre") String nombre);

    /**
     * NUEVO MÉTODO:
     * Busca la cantidad exacta filtrando por el ID del producto y el ID de la sede.
     */
    @Query("SELECT i.cantidad FROM Inventario i WHERE i.producto.idProducto = :idP AND i.sede.idSede = :idS")
    Optional<Integer> obtenerStockPorProductoYSede(@Param("idP") Integer idProducto, @Param("idS") Integer idSede);
}
