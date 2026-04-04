package com.luxStock.demo.services;

import com.luxStock.demo.model.dto.UsuarioEmpleadoDTO;
import com.luxStock.demo.model.entity.Rol;
import java.util.List;

public interface UsuarioService {
    void guardarUsuarioEmpleado(UsuarioEmpleadoDTO dto);
    List<Rol> obtenerTodosLosRoles();
    List<UsuarioEmpleadoDTO> obtenerTodosLosUsuariosDTO();
    boolean existePorDocumento(String documento);
    UsuarioEmpleadoDTO obtenerUsuarioPorId(Integer id);
}
