package com.luxStock.demo.controller.api;

import com.luxStock.demo.model.dto.PedidoDTO;
import com.luxStock.demo.model.dto.ProductoDTO;
import com.luxStock.demo.model.dto.SedeDTO;
import com.luxStock.demo.model.dto.UsuarioEmpleadoDTO;
import com.luxStock.demo.security.dto.UsuarioSecurityDTO;
import com.luxStock.demo.services.InventarioService;
import com.luxStock.demo.services.PedidoService;
import com.luxStock.demo.services.ProductoService;
import com.luxStock.demo.services.SedeService;
import com.luxStock.demo.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class restController {
    private final SedeService sedeService;
    private final UsuarioService usuarioService;
    private final PedidoService pedidoService;
    private final InventarioService inventarioService;
    private final ProductoService productoService;

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

    @PostMapping("/guardarusuario")
    public ResponseEntity<String> guardarUsuario(@ModelAttribute UsuarioEmpleadoDTO usuarioDTO) {
        try{
            usuarioService.guardarUsuarioEmpleado(usuarioDTO);
            return ResponseEntity.ok("Usuario guardado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al guardar el usuario: " + e.getMessage());
        }
    }

    @PostMapping("registrarPedido")
    public ResponseEntity<String> registroPedidos(@ModelAttribute PedidoDTO pedidoDTO, @AuthenticationPrincipal UsuarioSecurityDTO usuarioLogueado){
        try {
            Integer idEmpleado = usuarioLogueado.getIdEmpleado();
            Integer idSede = usuarioLogueado.getIdSede();
            pedidoDTO.setIdEmpleado(idEmpleado);
            pedidoDTO.setIdSede(idSede);
            pedidoService.registroPedidos(pedidoDTO);
            return ResponseEntity.ok("Pedido creado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al guardar el pedido: " + e.getMessage());
        }
    }

    @PostMapping("/guardarProducto")
    public ResponseEntity<String> guardarProducto(@ModelAttribute ProductoDTO productoDTO) {
        try {
            productoService.guardarProducto(productoDTO);
            return ResponseEntity.ok("Producto guardado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al guardar el producto: " + e.getMessage());
        }
    }

    @GetMapping("/validar-documento")
    public ResponseEntity<Boolean> validarDocumento(@RequestParam String documento) {
        return ResponseEntity.ok(usuarioService.existePorDocumento(documento));
    }
    @PostMapping("/actualizarCantidadInventario")
    public ResponseEntity<String> actualizarCantidadInventario(
            @RequestParam Integer idInventario,
            @RequestParam Integer cantidad) {

        try {
            inventarioService.actualizarCantidad(idInventario, cantidad);
            return ResponseEntity.ok("Cantidad actualizada correctamente");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error al actualizar la cantidad: " + e.getMessage());
        }
    }

}
