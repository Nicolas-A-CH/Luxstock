package com.luxStock.demo.services.impl;

import com.luxStock.demo.model.dto.VentaDTO;
import com.luxStock.demo.model.entity.Empleado;
import com.luxStock.demo.model.entity.Pedido;
import com.luxStock.demo.model.entity.Venta;
import com.luxStock.demo.model.enums.EstadoPedido;
import com.luxStock.demo.repository.EmpleadoRepository;
import com.luxStock.demo.repository.PedidoRepository;
import com.luxStock.demo.repository.VentaRepository;
import com.luxStock.demo.services.VentaService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VentaServiceImpl implements VentaService {

    private final VentaRepository ventaRepository;
    private final PedidoRepository pedidoRepository;
    private final EmpleadoRepository empleadoRepository;

    @Override
    public List<VentaDTO> obtenerTodasLasVentas() {
        return ventaRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    public VentaDTO obtenerVentaPorId(Integer id) {
        return ventaRepository.findById(id)
                .map(this::convertirADTO)
                .orElse(null);
    }

    @Override
    @Transactional
    public void registrarVenta(VentaDTO ventaDTO) {
        Venta venta = new Venta();
        venta.setFecha(LocalDateTime.now());
        
        Empleado empleado = empleadoRepository.findByIdEmpleado(ventaDTO.getIdEmpleado());
        venta.setEmpleado(empleado);
        
        venta.setIdPedido(ventaDTO.getIdPedido());
        venta.setTotal(ventaDTO.getTotal());
        venta.setMedioPago(ventaDTO.getMedioPago());
        
        ventaRepository.save(venta);
        
        // Cambiar estado del pedido a PAGADO
        Pedido pedido = pedidoRepository.findByIdPedido(ventaDTO.getIdPedido());
        if (pedido != null) {
            pedido.setEstado(EstadoPedido.PAGADO.getNombreEstado());
            pedidoRepository.save(pedido);
        }
    }

    private VentaDTO convertirADTO(Venta venta) {
        VentaDTO dto = new VentaDTO();
        dto.setIdVenta(venta.getIdVenta());
        dto.setFecha(venta.getFecha());
        if (venta.getEmpleado() != null) {
            dto.setIdEmpleado(venta.getEmpleado().getIdEmpleado());
            dto.setNombreEmpleado(venta.getEmpleado().getNombre() + " " + venta.getEmpleado().getApellido());
        }
        dto.setIdPedido(venta.getIdPedido());
        dto.setTotal(venta.getTotal());
        dto.setMedioPago(venta.getMedioPago());
        return dto;
    }
}
