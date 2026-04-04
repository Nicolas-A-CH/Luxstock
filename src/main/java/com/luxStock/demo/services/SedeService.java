package com.luxStock.demo.services;

import com.luxStock.demo.model.dto.SedeDTO;

import java.util.List;

public interface SedeService {
    void guardarSede(SedeDTO sedeDTO);
    List<SedeDTO> obtenerTodasLasSedesDTO();
    SedeDTO obtenerSedePorId(Integer id);
}
