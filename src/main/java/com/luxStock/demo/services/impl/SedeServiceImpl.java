package com.luxStock.demo.services.impl;

import com.luxStock.demo.dto.SedeDTO;
import com.luxStock.demo.entity.Sede;
import com.luxStock.demo.repository.SedeRepository;
import com.luxStock.demo.services.SedeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SedeServiceImpl implements SedeService {

    private final SedeRepository sedeRepository;

    public SedeServiceImpl(SedeRepository sedeRepository) {
        this.sedeRepository = sedeRepository;
    }

    @Override
    public void guardarSede(SedeDTO sedeDTO) {
        Sede sedeEntity = new Sede();

        // Si el idSede no es null, se tratará de una actualización; si es null, de una creación
        sedeEntity.setIdSede(sedeDTO.getIdSede());
        sedeEntity.setNombre(sedeDTO.getNombre());
        sedeEntity.setDireccion(sedeDTO.getDireccion());
        sedeEntity.setCiudad(sedeDTO.getCiudad());
        sedeEntity.setTelefono(sedeDTO.getTelefono());

        // Guardamos en la base de datos
        sedeRepository.save(sedeEntity);

    }

    @Override
    public List<SedeDTO> obtenerTodasLasSedesDTO() {
        List<Sede> sedes = sedeRepository.findAll();

        return sedes.stream().map(sede -> new SedeDTO(
                sede.getIdSede(),
                sede.getNombre(),
                sede.getDireccion(),
                sede.getCiudad(),
                sede.getTelefono()
        )).collect(Collectors.toList());
    }
}
