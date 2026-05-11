package com.luxStock.demo.model.entity;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "productos")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Integer idProducto;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 50)
    private String tipo;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;
}
