package com.luxStock.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoDTO {
    private Integer idPedido;
    private LocalDateTime fecha;
    private Integer idEmpleado;
    private Integer idSede;
    private Integer mesa;
    private String estado;
    private List<DetallePedidoDTO> detalles;
}
