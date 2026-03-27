package com.luxStock.demo.controller.api;

import com.luxStock.demo.dto.SedeDTO;
import com.luxStock.demo.services.SedeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> guardarSede(@ModelAttribute SedeDTO sedeDTO) {
        try {
            // Llamamos al servicio para guardar (crear o actualizar)
            sedeService.guardarSede(sedeDTO);

            // Respondemos con un 200 OK para que el 'response.ok' del fetch en JS sea verdadero
            return ResponseEntity.ok("Sede guardada exitosamente");

        } catch (Exception e) {
            // Si algo falla en la base de datos, enviamos un error 500
            // Esto hará que el JS caiga en el bloque 'else' y muestre el SweetAlert de error
            return ResponseEntity.internalServerError().body("Error al guardar la sede: " + e.getMessage());
        }
    }
}
