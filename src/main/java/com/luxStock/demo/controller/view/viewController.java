package com.luxStock.demo.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/luxbar")
public class viewController {
    @GetMapping("/usuarios")
    public String viewIndexPage(){
        return "listadoUsuario.html";
    }
    @GetMapping("/cerar usuario")
    public String viewFormularioUsuarioPage(){
        return "listadoUsuario.html";
    }
    @GetMapping("/sedes")
    public String viewSedesPage(){
        return "listadoUsuario.html";
    }
}
