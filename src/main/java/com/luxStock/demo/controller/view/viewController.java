package com.luxStock.demo.controller.view;

import com.luxStock.demo.entity.Sede;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/luxbar")
public class viewController {
    @GetMapping("/usuarios")
    public String viewIndexPage(){
        return "listadoUsuario.html";
    }
    @GetMapping("/crear usuario")
    public String viewFormularioUsuarioPage(){
        return "listadoUsuario.html";
    }
    @GetMapping("/sedes")
    public String viewSedesPage(){
        return "listadoSede.html";
    }
    @GetMapping("/crear sede")
    public String viewformularioSedePage(Model model){
        model.addAttribute("sede", new Sede());
        return "formularioSedes.html";
    }
}
