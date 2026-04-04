package com.luxStock.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SedeDTO {
    private Integer idSede;
    private String nombre;
    private String direccion;
    private String ciudad;
    private String telefono;
}
