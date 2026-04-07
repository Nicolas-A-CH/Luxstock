package com.luxStock.demo.services;

import com.luxStock.demo.model.entity.Inventario;

import java.util.List;

public interface InventarioService {

    List<Inventario> obtenerTodos();

    void guardar(Inventario inventario);

    Inventario obtenerPorId(Integer id);

    void eliminar(Integer id);

    void actualizarCantidad(Integer idIventario, Integer nuevaCantidad);

    List<Inventario> buscarPorProducto(String nombre);
}