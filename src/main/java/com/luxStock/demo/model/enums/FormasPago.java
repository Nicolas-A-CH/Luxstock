package com.luxStock.demo.model.enums;

import lombok.Getter;

@Getter
public enum FormasPago {
    EFECTIVO("Efectivo"),
    TARJETA("Tarjeta"),
    CREDITO("Credito");

    private final String nombreForma;
    FormasPago(String nombreForma) {
        this.nombreForma = nombreForma;
    }
}
