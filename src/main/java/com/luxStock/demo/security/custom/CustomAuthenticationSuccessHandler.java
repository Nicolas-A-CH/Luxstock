package com.luxStock.demo.security.custom;

import com.luxStock.demo.model.entity.Usuario;
import com.luxStock.demo.repository.UsuarioRepository;
import com.luxStock.demo.security.Service.JwtService;
import com.luxStock.demo.security.dto.UsuarioSecurityDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationSuccessHandler.class); // Instanciar Logger

    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        UsuarioSecurityDTO usuarioDTO = (UsuarioSecurityDTO) authentication.getPrincipal();

        String token = jwtService.generateToken(usuarioDTO);
        logger.info("Token JWT generado: {}", token);

        Cookie cookie = new Cookie("jwt", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // Cambiar a true si usas HTTPS
        cookie.setPath("/");
        cookie.setMaxAge(86400); // 1 día

        response.addCookie(cookie);
        logger.info("Cookie 'jwt' añadida a la respuesta.");

        response.sendRedirect("/luxbar/dashboard");
        logger.info("Redirigiendo a /luxbar/dashboard");
    }
}