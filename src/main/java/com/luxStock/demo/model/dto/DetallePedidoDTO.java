package com.luxStock.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetallePedidoDTO {
    private Integer idDetalle;
    private Integer idProducto;
    private Integer cantidad;
}
