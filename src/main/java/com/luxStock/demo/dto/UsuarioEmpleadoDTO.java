package com.luxStock.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioEmpleadoDTO {
    // Datos del Empleado
    private Integer idEmpleado;
    private String nombre;
    private String apellido;
    private String documento;
    private String telefono;
    private Integer idRol;
    private String nombreRol;
    private Integer idSede;
    private String nombreSede;

    // Datos del Usuario
    private String username;
    private String password;
}
