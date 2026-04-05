package com.luxStock.demo.repository;

import com.luxStock.demo.model.entity.DetallePedido;
import com.luxStock.demo.model.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Integer> {
    List<DetallePedido> findByPedido(Pedido pedido);
}
