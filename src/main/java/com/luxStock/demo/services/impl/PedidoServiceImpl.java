package com.luxStock.demo.services.impl;

import com.luxStock.demo.model.dto.DetallePedidoDTO;
import com.luxStock.demo.model.dto.PedidoDTO;
import com.luxStock.demo.model.entity.*;
import com.luxStock.demo.model.enums.EstadoPedido;
import com.luxStock.demo.repository.*;
import com.luxStock.demo.services.PedidoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final SedeRepository sedeRepository;
    private final EmpleadoRepository empleadoRepository;
    private final DetallePedidoRepository detallePedidoRepository;
    private final ProductoRepository productoRepository;

    @Override
    @Transactional
    public void registroPedidos(PedidoDTO pedidoDTO) {
        try {
            Pedido pedidoEntity;

            // Si el ID existe, intentamos buscarlo para editar, si no, creamos uno nuevo
            if (pedidoDTO.getIdPedido() != null && pedidoRepository.existsById(pedidoDTO.getIdPedido())) {
                pedidoEntity = pedidoRepository.findByIdPedido(pedidoDTO.getIdPedido());
            } else {
                pedidoEntity = new Pedido();
                pedidoEntity.setIdPedido(pedidoDTO.getIdPedido());
                pedidoEntity.setEstado(EstadoPedido.SOLICITADO.getNombreEstado());
            }

            Sede sede = sedeRepository.findByIdSede(pedidoDTO.getIdSede());
            pedidoEntity.setSede(sede);

            Empleado empleado = empleadoRepository.findByIdEmpleado(pedidoDTO.getIdEmpleado());
            pedidoEntity.setEmpleado(empleado);

            if (pedidoDTO.getFecha() != null) {
                pedidoEntity.setFecha(pedidoDTO.getFecha());
            }

            if (pedidoDTO.getMesa() != null) {
                pedidoEntity.setMesa(pedidoDTO.getMesa());
            }

            if (pedidoDTO.getEstado() != null) {
                pedidoEntity.setEstado(pedidoDTO.getEstado());
            }

            // Guardamos o actualizamos el pedido principal
            Pedido savedPedido = pedidoRepository.save(pedidoEntity);

            // Procesar detalles
            if (pedidoDTO.getDetalles() != null && !pedidoDTO.getDetalles().isEmpty()) {
                for (DetallePedidoDTO detalleDTO : pedidoDTO.getDetalles()) {
                    DetallePedido detalleEntity;

                    // Si tiene ID de detalle, buscamos para editar
                    if (detalleDTO.getIdDetalle() != null && detallePedidoRepository.existsById(detalleDTO.getIdDetalle())) {
                        detalleEntity = detallePedidoRepository.findById(detalleDTO.getIdDetalle()).orElse(new DetallePedido());
                    } else {
                        detalleEntity = new DetallePedido();
                        detalleEntity.setIdDetalle(detalleDTO.getIdDetalle());
                    }

                    detalleEntity.setPedido(savedPedido);

                    Producto producto = productoRepository.findByIdProducto(detalleDTO.getIdProducto());
                    detalleEntity.setProducto(producto);
                    detalleEntity.setCantidad(detalleDTO.getCantidad());

                    detallePedidoRepository.save(detalleEntity);
                }
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<PedidoDTO> ObtenerTodosLosPedidos() {
        List<Pedido> pedidos = pedidoRepository.findAll();
        
        return pedidos.stream().map(pedido -> {
            // Mapear los detalles del pedido
            List<DetallePedido> detallesEntity = detallePedidoRepository.findByPedido(pedido);
            List<DetallePedidoDTO> detallesDTO = detallesEntity.stream().map(detalle -> 
                new DetallePedidoDTO(
                    detalle.getIdDetalle(),
                    detalle.getProducto() != null ? detalle.getProducto().getIdProducto() : null,
                    detalle.getCantidad()
                )
            ).collect(Collectors.toList());

            return new PedidoDTO(
                pedido.getIdPedido(),
                pedido.getFecha(),
                pedido.getEmpleado() != null ? pedido.getEmpleado().getIdEmpleado() : null,
                pedido.getSede() != null ? pedido.getSede().getIdSede() : null,
                pedido.getMesa(),
                pedido.getEstado(),
                detallesDTO
            );
        }).collect(Collectors.toList());
    }
}
