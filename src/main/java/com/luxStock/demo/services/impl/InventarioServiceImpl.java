package com.luxStock.demo.services.impl;

import com.luxStock.demo.model.entity.Inventario;
import com.luxStock.demo.repository.InventarioRepository;
import com.luxStock.demo.services.InventarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventarioServiceImpl implements InventarioService {

    private final InventarioRepository inventarioRepository;

    @Override
    public List<Inventario> obtenerTodos() {
        return inventarioRepository.findAll();
    }

    @Override
    public void guardar(Inventario inventario) {
        inventarioRepository.save(inventario);
    }

    @Override
    public Inventario obtenerPorId(Integer id) {
        return inventarioRepository.findById(id).orElse(null);
    }

    @Override
    public void eliminar(Integer id) {
        inventarioRepository.deleteById(id);
    }
    @Override
    public void actualizarCantidad(Integer idInventario, Integer nuevaCantidad) {
        Inventario inventario = inventarioRepository.findById(idInventario)
                .orElseThrow(() -> new RuntimeException("Inventario no encontrado"));

        inventario.setCantidad(nuevaCantidad);
        inventarioRepository.save(inventario);
    }

    @Override
    public List<Inventario> buscarPorProducto(String nombre) {
        return inventarioRepository.findByProductoNombreContainingIgnoreCase(nombre);
    }
}