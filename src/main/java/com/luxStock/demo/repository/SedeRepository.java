package com.luxStock.demo.repository;

import com.luxStock.demo.model.entity.Sede;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SedeRepository extends JpaRepository<Sede, Integer> {
    Sede findByIdSede(Integer idSede);
}
