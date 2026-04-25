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

    /**
     * Consulta el stock real en la tabla 'inventario'
     * filtrando por el ID del producto y el ID de la sede.
     */
    @Override
    public Integer obtenerStockDisponible(Integer idProducto, Integer idSede) {
        if (idProducto == null || idSede == null) {
            return 0;
        }

        // Usamos el repositorio para buscar la cantidad real en la DB
        return inventarioRepository.obtenerStockPorProductoYSede(idProducto, idSede)
                .orElse(0); // Si no existe el registro en esa sede, el stock es 0
    }
}
