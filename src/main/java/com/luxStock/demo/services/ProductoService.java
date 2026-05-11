package com.luxStock.demo.services;

import com.luxStock.demo.model.dto.ProductoDTO;

import java.util.List;

public interface ProductoService {
    List<ProductoDTO> ObtenerTodosProductors();
    Integer obtenerStockDisponible(Integer idProducto, Integer idSede);
    void guardarProducto(ProductoDTO productoDTO);
    ProductoDTO obtenerProductoPorId(Integer id);
}