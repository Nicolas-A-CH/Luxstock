package com.luxStock.demo.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/webjars/**", "/css/**", "/js/**", "/images/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(login -> login
                        // 3. Indicar cuál es nuestra página de login personalizada
                        .loginPage("/login")
                        // 4. A dónde ir tras un login exitoso (tu futuro dashboard)
                        .defaultSuccessUrl("/dashboard", true)
                        .permitAll()
                )
                .formLogin(login -> login
                        // ESTA LÍNEA le dice a Spring: "No uses tu login genérico, redirige a mi ruta /login"
                        .loginPage("/login")

                        // Y esta le dice a dónde ir después de un login exitoso
                        .defaultSuccessUrl("/dashboard", true)
                        .permitAll()
                );

        return http.build();
    }
}
