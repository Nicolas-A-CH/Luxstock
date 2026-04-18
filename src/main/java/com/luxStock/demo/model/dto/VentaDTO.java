package com.luxStock.demo.model.dto;

import com.luxStock.demo.model.entity.Empleado;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VentaDTO {
    private Integer idVenta;
    private LocalDateTime fecha;
    private Integer idEmpleado;
    private String nombreEmpleado; // Para mostrar el nombre completo del empleado
    private Integer idPedido;
    private BigDecimal total;
    private String medioPago;
}
