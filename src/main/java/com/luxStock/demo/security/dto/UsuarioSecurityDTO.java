package com.luxStock.demo.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioSecurityDTO implements UserDetails {
    private String username;
    private String password;
    private List<SimpleGrantedAuthority> authorities;
    private Integer idEmpleado;
    private String nombreSede;
    private String rol;

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
