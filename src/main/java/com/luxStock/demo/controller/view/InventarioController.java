package com.luxStock.demo.controller.view;

import com.luxStock.demo.model.entity.Inventario;
import com.luxStock.demo.services.InventarioService;
import com.luxStock.demo.services.ProductoService;
import com.luxStock.demo.services.SedeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class InventarioController {

    private final InventarioService inventarioService;
    private final ProductoService productoService;
    private final SedeService sedeService;

    @GetMapping("/inventario")
    public String listar(@RequestParam(value = "search", required = false) String search, Model model){
        List<Inventario> inventarios;
        if (search != null && !search.trim().isEmpty()) {
            inventarios = inventarioService.buscarPorProducto(search.trim());
        } else {
            inventarios = inventarioService.obtenerTodos();
        }
        model.addAttribute("inventarios", inventarios);
        model.addAttribute("search", search); // to keep the search value in the input
        return "listadoInventario";
    }

    @GetMapping("/inventario/nuevo")
    public String formulario(Model model){
        model.addAttribute("inventario", new Inventario());
        model.addAttribute("productos", productoService.ObtenerTodosProductors());
        model.addAttribute("sedes", sedeService.obtenerTodasLasSedesDTO());
        return "Inventario";
    }

    @PostMapping("/inventario")
    public String guardar(@ModelAttribute Inventario inventario){
        inventarioService.guardar(inventario);
        return "redirect:/inventario";
    }

    @GetMapping("/inventario/eliminar/{id}")
    public String eliminar(@PathVariable Integer id){
        inventarioService.eliminar(id);
        return "redirect:/inventario";
    }
}