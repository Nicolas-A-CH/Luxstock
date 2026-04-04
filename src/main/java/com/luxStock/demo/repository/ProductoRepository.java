package com.luxStock.demo.repository;

import com.luxStock.demo.model.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    Producto findByIdProducto(Integer idProducto);
}
