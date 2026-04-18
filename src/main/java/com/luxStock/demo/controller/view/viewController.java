package com.luxStock.demo.controller.view;

import com.luxStock.demo.model.dto.*;
import com.luxStock.demo.model.entity.Sede;
import com.luxStock.demo.model.enums.EstadoPedido;
import com.luxStock.demo.model.enums.FormasPago;
import com.luxStock.demo.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/luxbar")
@RequiredArgsConstructor
public class viewController {
    private final SedeService sedeService;
    private final UsuarioService usuarioService;
    private final PedidoService pedidoService;
    private final ProductoService productoService;
    private final VentaService ventaService;

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

    @GetMapping("/pedidos")
    public String viewPedidosPage(Model model){
        model.addAttribute("pedidos", pedidoService.ObtenerTodosLosPedidos());
        Map<Integer, String> mapaEmpleados = usuarioService.obtenerTodosLosUsuariosDTO().stream()
                .collect(Collectors.toMap(
                        UsuarioEmpleadoDTO::getIdEmpleado,
                        usuario -> usuario.getNombre() + " " + usuario.getApellido()
                ));
        model.addAttribute("mapaEmpleados", mapaEmpleados);
        return "listadoPedidos";
    }
    
    @GetMapping("/crear pedidos")
    public String viewFormularioPedidosPage(Model model){
        PedidoDTO pedidoDTO = new PedidoDTO();
        pedidoDTO.setEstado(EstadoPedido.SOLICITADO.getNombreEstado());
        model.addAttribute("pedidoDTO", pedidoDTO);
        model.addAttribute("productos", productoService.ObtenerTodosProductors());
        model.addAttribute("isEdit", false);
        return "formularioPedidos";
    }

    @GetMapping("/editar pedido/{id}")
    public String viewFormularioEdicionPedidoPage(@PathVariable Integer id, Model model){
        List<PedidoDTO> pedidos = pedidoService.ObtenerTodosLosPedidos();
        PedidoDTO pedidoDTO = pedidos.stream()
                .filter(p -> p.getIdPedido().equals(id))
                .findFirst()
                .orElse(null);
        
        if(pedidoDTO != null) {
            model.addAttribute("pedidoDTO", pedidoDTO);
            model.addAttribute("productos", productoService.ObtenerTodosProductors());
            model.addAttribute("sedes", sedeService.obtenerTodasLasSedesDTO());
            model.addAttribute("empleados", usuarioService.obtenerTodosLosUsuariosDTO());
            model.addAttribute("estados", Arrays.stream(EstadoPedido.values())
                    .map(EstadoPedido::getNombreEstado)
                    .collect(Collectors.toList()));
            model.addAttribute("isEdit", true);
        } else {
            return "redirect:/luxbar/pedidos";
        }
        return "formularioPedidos";
    }

    @GetMapping("/ventas")
    public String viewVentasPage(Model model){
        model.addAttribute("pedidos", pedidoService.ObtenerTodosLosPedidos());
        Map<Integer, String> mapaEmpleados = usuarioService.obtenerTodosLosUsuariosDTO().stream()
                .collect(Collectors.toMap(
                        UsuarioEmpleadoDTO::getIdEmpleado,
                        usuario -> usuario.getNombre() + " " + usuario.getApellido()
                ));
        model.addAttribute("mapaEmpleados", mapaEmpleados);
        
        Map<Integer, String> mapaSedes = sedeService.obtenerTodasLasSedesDTO().stream()
                .collect(Collectors.toMap(
                        SedeDTO::getIdSede,
                        SedeDTO::getNombre
                ));
        model.addAttribute("mapaSedes", mapaSedes);
        
        return "listadoVentas";
    }

    @GetMapping("/crear venta/{idPedido}")
    public String viewFormularioVentaPage(@PathVariable Integer idPedido, Model model){
        PedidoDTO pedidoDTO = pedidoService.ObtenerTodosLosPedidos().stream()
                .filter(p -> p.getIdPedido().equals(idPedido))
                .findFirst()
                .orElse(null);
        
        if(pedidoDTO != null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            UsuarioEmpleadoDTO usuarioLogueado = usuarioService.obtenerTodosLosUsuariosDTO().stream()
                    .filter(u -> u.getUsername().equals(username))
                    .findFirst()
                    .orElse(null);

            VentaDTO ventaDTO = new VentaDTO();
            ventaDTO.setIdPedido(pedidoDTO.getIdPedido());
            if (usuarioLogueado != null) {
                ventaDTO.setIdEmpleado(usuarioLogueado.getIdEmpleado());
                model.addAttribute("sedeUsuario", usuarioLogueado.getNombreSede());
                model.addAttribute("idSedeUsuario", usuarioLogueado.getIdSede());
            }

            // Calcular total
            List<ProductoDTO> productos = productoService.ObtenerTodosProductors();
            Map<Integer, BigDecimal> precios = productos.stream()
                    .collect(Collectors.toMap(ProductoDTO::getIdProducto, ProductoDTO::getPrecio));
            
            BigDecimal total = pedidoDTO.getDetalles().stream()
                    .map(d -> {
                        BigDecimal precio = precios.get(d.getIdProducto());
                        return precio != null ? precio.multiply(new BigDecimal(d.getCantidad())) : BigDecimal.ZERO;
                    })
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            ventaDTO.setTotal(total);
            
            model.addAttribute("ventaDTO", ventaDTO);
            model.addAttribute("pedido", pedidoDTO);
            model.addAttribute("mapaProductos", productos.stream()
                    .collect(Collectors.toMap(ProductoDTO::getIdProducto, ProductoDTO::getNombre)));
            model.addAttribute("mapaPrecios", precios);
            model.addAttribute("formasPago", FormasPago.values());
            
            return "formularioVenta";
        }
        return "redirect:/luxbar/ventas";
    }

    @PostMapping("/guardar venta")
    public String guardarVenta(@ModelAttribute VentaDTO ventaDTO) {
        ventaService.registrarVenta(ventaDTO);
        return "redirect:/luxbar/ventas";
    }
}
