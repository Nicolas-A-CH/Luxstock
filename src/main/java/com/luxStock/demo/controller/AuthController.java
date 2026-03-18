package com.luxStock.demo.controller;

import com.luxStock.demo.entity.Usuario;
import com.luxStock.demo.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/registro")
    public String registroPage() {
        return "registro";
    }

    @PostMapping("/registro")
    public String registrar(
            @RequestParam String username,
            @RequestParam String password,
            Model model) {

        if (usuarioRepository.findByUsername(username).isPresent()) {
            model.addAttribute("error", "El usuario ya existe");
            return "registro";
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(username);
        usuario.setPassword(passwordEncoder.encode(password));
        usuario.setRol("ROLE_USER");
        usuarioRepository.save(usuario);

        return "redirect:/login?registered=true";
    }
}
