package com.luxStock.demo.controller.view;

import com.luxStock.demo.entity.Sede;
import com.luxStock.demo.services.SedeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/luxbar")
@RequiredArgsConstructor
public class viewController {
    private final SedeService sedeService;
    @GetMapping("/usuarios")
    public String viewIndexPage(){
        return "listadoUsuario";
    }
    @GetMapping("/crear usuario")
    public String viewFormularioUsuarioPage(){
        return "listadoUsuario";
    }
    @GetMapping("/sedes")
    public String viewSedesPage(Model model){
        model.addAttribute("sedes", sedeService.obtenerTodasLasSedesDTO());
        return "listadoSede";
    }
    @GetMapping("/crear sede")
    public String viewformularioSedePage(Model model){
        model.addAttribute("sede", new Sede());
        return "formularioSedes";
    }
}
