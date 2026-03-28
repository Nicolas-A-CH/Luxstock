package com.luxStock.demo.services.impl;

import com.luxStock.demo.dto.UsuarioEmpleadoDTO;
import com.luxStock.demo.entity.Empleado;
import com.luxStock.demo.entity.Rol;
import com.luxStock.demo.entity.Sede;
import com.luxStock.demo.entity.Usuario;
import com.luxStock.demo.repository.EmpleadoRepository;
import com.luxStock.demo.repository.RolRepository;
import com.luxStock.demo.repository.SedeRepository;
import com.luxStock.demo.repository.UsuarioRepository;
import com.luxStock.demo.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final EmpleadoRepository empleadoRepository;
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final SedeRepository sedeRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void guardarUsuarioEmpleado(UsuarioEmpleadoDTO dto) {
        Empleado empleado = new Empleado();
        empleado.setIdEmpleado(dto.getIdEmpleado());
        empleado.setNombre(dto.getNombre());
        empleado.setApellido(dto.getApellido());
        empleado.setDocumento(dto.getDocumento());
        empleado.setTelefono(dto.getTelefono());

        Rol rol = rolRepository.findById(dto.getIdRol()).orElseThrow();
        Sede sede = sedeRepository.findById(dto.getIdSede()).orElseThrow();

        empleado.setRol(rol);
        empleado.setSede(sede);

        empleado = empleadoRepository.save(empleado);

        Usuario usuario = new Usuario();
        usuario.setIdUsuario(empleado.getIdEmpleado());
        usuario.setUsername(dto.getUsername());
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        usuario.setEmpleado(empleado);

        usuarioRepository.save(usuario);
    }

    @Override
    public List<Rol> obtenerTodosLosRoles() {
        return rolRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioEmpleadoDTO> obtenerTodosLosUsuariosDTO() {
        return usuarioRepository.findAll().stream()
                .map(this::convertirA_DTO)
                .collect(Collectors.toList());
    }

    private UsuarioEmpleadoDTO convertirA_DTO(Usuario usuario) {
        Empleado empleado = usuario.getEmpleado();
        return UsuarioEmpleadoDTO.builder()
                .idEmpleado(empleado.getIdEmpleado())
                .nombre(empleado.getNombre())
                .apellido(empleado.getApellido())
                .documento(empleado.getDocumento())
                .telefono(empleado.getTelefono())
                .idRol(empleado.getRol() != null ? empleado.getRol().getIdRol() : null)
                .nombreRol(empleado.getRol() != null ? empleado.getRol().getNombre() : "N/A")
                .idSede(empleado.getSede() != null ? empleado.getSede().getIdSede() : null)
                .nombreSede(empleado.getSede() != null ? empleado.getSede().getNombre() : "N/A")
                .username(usuario.getUsername())
                .build();
    }
}
