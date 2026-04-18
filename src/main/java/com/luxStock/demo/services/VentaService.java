package com.luxStock.demo.services;

import com.luxStock.demo.model.dto.VentaDTO;
import java.util.List;

public interface VentaService {
    List<VentaDTO> obtenerTodasLasVentas();
    VentaDTO obtenerVentaPorId(Integer id);
    void registrarVenta(VentaDTO ventaDTO);
}
