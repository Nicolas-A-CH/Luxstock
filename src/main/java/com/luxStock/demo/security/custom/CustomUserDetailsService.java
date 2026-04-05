package com.luxStock.demo.security.custom;

import com.luxStock.demo.model.entity.Usuario;
import com.luxStock.demo.repository.UsuarioRepository;
import com.luxStock.demo.security.dto.UsuarioSecurityDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        // Al estar dentro de @Transactional, Hibernate puede inicializar los proxies de Empleado y Rol
        String rolNombre = usuario.getEmpleado().getRol().getNombre();
        Integer idEmpleado = usuario.getEmpleado().getIdEmpleado();
        Integer idSede = usuario.getEmpleado().getSede().getIdSede();

        return new UsuarioSecurityDTO(
                usuario.getUsername(),
                usuario.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + rolNombre.toUpperCase())),
                idEmpleado,
                idSede,
                rolNombre
        );
    }
}
