package com.luxStock.demo.services.impl;

import com.luxStock.demo.model.dto.ProductoDTO;
import com.luxStock.demo.model.entity.Producto;
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
}
