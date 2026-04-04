package com.luxStock.demo.controller.view;

import com.luxStock.demo.model.dto.SedeDTO;
import com.luxStock.demo.model.dto.UsuarioEmpleadoDTO;
import com.luxStock.demo.model.entity.Sede;
import com.luxStock.demo.services.SedeService;
import com.luxStock.demo.services.UsuarioService;
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
    private final UsuarioService usuarioService;

    @GetMapping("/dashboard")
    public String viewDashboardPage(){
        return "dashboard";
    }

    @GetMapping("/usuarios")
    public String viewIndexPage(Model model){
        model.addAttribute("usuariosEmpleados", usuarioService.obtenerTodosLosUsuariosDTO());
        return "listadoUsuario";
    }

    @GetMapping("/crear usuario")
    public String viewFormularioUsuarioPage(Model model){
        model.addAttribute("usuarioDTO", new UsuarioEmpleadoDTO());
        model.addAttribute("roles", usuarioService.obtenerTodosLosRoles());
        model.addAttribute("sedes", sedeService.obtenerTodasLasSedesDTO());
        return "formularioUsuarios";
    }

    @GetMapping("/editar usuario/{id}")
    public String viewFormularioEdicionUsuarioPage(@PathVariable Integer id, Model model){
        UsuarioEmpleadoDTO usuarioEmpleadoDTO = usuarioService.obtenerUsuarioPorId(id);
        if(usuarioEmpleadoDTO != null) {
            model.addAttribute("usuarioDTO", usuarioEmpleadoDTO);
            model.addAttribute("roles", usuarioService.obtenerTodosLosRoles());
            model.addAttribute("sedes", sedeService.obtenerTodasLasSedesDTO());
        } else {
            return "redirect:/luxbar/usuarios";
        }
        return "formularioUsuarios";
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
        SedeDTO sedeDTO = sedeService.obtenerSedePorId(id);
        if(sedeDTO != null) {
            model.addAttribute("sede", sedeDTO);
        } else {
            return "redirect:/luxbar/sedes";
        }
        return "formularioSedes";
    }
}
