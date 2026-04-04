package com.luxStock.demo.repository;

import com.luxStock.demo.model.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
    Pedido findByIdPedido(Integer idPedido);
}
