package com.luxStock.demo.model.enums;

import lombok.Getter;

@Getter
public enum EstadoPedido {
    SOLICITADO("Solicitado"),
    CANCELADO("Cancelado"),
    PAGADO("Pagado");

    private final String nombreEstado;
    EstadoPedido(String nombreEstado) {
        this.nombreEstado = nombreEstado;
    }
}