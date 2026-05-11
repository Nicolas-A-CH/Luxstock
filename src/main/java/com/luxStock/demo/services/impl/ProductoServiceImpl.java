package com.luxStock.demo.services.impl;

import com.luxStock.demo.model.dto.ProductoDTO;
import com.luxStock.demo.model.entity.Producto;
import com.luxStock.demo.repository.InventarioRepository; // Asegúrate de tener este repo creado
import com.luxStock.demo.repository.ProductoRepository;
import com.luxStock.demo.services.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final InventarioRepository inventarioRepository; // Inyectamos el repo de inventario

    @Override
    public List<ProductoDTO> ObtenerTodosProductors() {
        List<Producto> productos = productoRepository.findAll();

        return productos.stream().map(producto -> new ProductoDTO(
                producto.getIdProducto(),
                producto.getNombre(),
                producto.getTipo(),
                producto.getPrecio()
        )).collect(Collectors.toList());
    }

    @Override
    public Integer obtenerStockDisponible(Integer idProducto, Integer idSede) {
        if (idProducto == null || idSede == null) {
            return 0;
        }
        return inventarioRepository.obtenerStockPorProductoYSede(idProducto, idSede)
                .orElse(0);
    }

    @Override
    public void guardarProducto(ProductoDTO productoDTO) {
        Producto producto;
        if (productoDTO.getIdProducto() != null && productoRepository.existsById(productoDTO.getIdProducto())) {
            producto = productoRepository.findByIdProducto(productoDTO.getIdProducto());
        } else {
            producto = new Producto();
        }
        producto.setNombre(productoDTO.getNombre());
        producto.setTipo(productoDTO.getTipo());
        producto.setPrecio(productoDTO.getPrecio());
        productoRepository.save(producto);
    }

    @Override
    public ProductoDTO obtenerProductoPorId(Integer id) {
        Producto producto = productoRepository.findByIdProducto(id);
        if (producto == null) return null;
        return new ProductoDTO(producto.getIdProducto(), producto.getNombre(), producto.getTipo(), producto.getPrecio());
    }
}
