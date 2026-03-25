package com.luxStock.demo.controller.api;

import com.luxStock.demo.dto.SedeDTO;
import com.luxStock.demo.services.SedeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class restController {
    private final SedeService sedeService;

    @PostMapping("/guardarSede")
    public String guardarSede(@ModelAttribute("sede") SedeDTO sedeDTO) {
        // Llamamos al servicio para guardar
        sedeService.guardarSede(sedeDTO);

        // Redirigimos al listado de sedes después de guardar (evita reenvío de formularios)
        return "redirect:/sedes";
    }
}
