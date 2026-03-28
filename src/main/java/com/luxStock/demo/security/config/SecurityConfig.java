package com.luxStock.demo.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Recursos estáticos, login y acceso denegado permitidos para todos
                        .requestMatchers("/", "/login", "/403", "/webjars/**", "/css/**", "/js/**", "/images/**").permitAll()
                        
                        // Restricciones de administración (Solo ADMINISTRADOR)
                        .requestMatchers("/luxbar/usuarios/**", "/luxbar/crear usuario/**", "/luxbar/editar usuario/**").hasRole("ADMINISTRADOR")
                        .requestMatchers("/luxbar/sedes/**", "/luxbar/crear sede/**", "/luxbar/editar sede/**").hasRole("ADMINISTRADOR")
                        .requestMatchers("/api/guardarusuario/**", "/api/guardarSede/**").hasRole("ADMINISTRADOR")

                        // Dashboard y otras rutas comunes permitidas para cualquier usuario logueado
                        .requestMatchers("/luxbar/dashboard").authenticated()
                        
                        // Cualquier otra petición requiere autenticación
                        .anyRequest().authenticated()
                )
                .formLogin(login -> login
                        .loginPage("/login")
                        .defaultSuccessUrl("/luxbar/dashboard", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                .exceptionHandling(exception -> exception
                        .accessDeniedPage("/403") // Redirigir a la nueva vista de error sin cerrar sesión
                );

        return http.build();
    }
}
