package com.luxStock.demo.controller.view;

import com.luxStock.demo.dto.SedeDTO;
import com.luxStock.demo.entity.Sede;
import com.luxStock.demo.services.SedeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    @GetMapping("/editar sede/{id}")
    public String viewFormularioEdicionSedePage(@PathVariable Integer id, Model model){
        // Buscamos los datos actuales de la sede
        SedeDTO sedeDTO = sedeService.obtenerSedePorId(id);

        // Pasamos la sede encontrada a la vista (si no existe, mandamos una vacía por seguridad)
        if(sedeDTO != null) {
            model.addAttribute("sede", sedeDTO);
        } else {
            return "redirect:/luxbar/sedes"; // Si intentan editar un ID que no existe, los devolvemos a la lista
        }

        // Retornamos exactamente la misma vista HTML
        return "formularioSedes";
    }
}
