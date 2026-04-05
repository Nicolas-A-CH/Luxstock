package com.luxStock.demo.services;

import com.luxStock.demo.model.dto.PedidoDTO;

import java.util.List;

public interface PedidoService {
    void registroPedidos(PedidoDTO pedidoDTO);
    List<PedidoDTO> ObtenerTodosLosPedidos();
}
