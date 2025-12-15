package com.gft.clinica_service.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gft.clinica_service.models.Doenca;
import com.gft.clinica_service.models.Sintoma;


public interface DoencaRepository extends JpaRepository<Doenca, UUID> {
    Optional<List<Doenca>> findBySintomas(List<Sintoma> sintomas);

    Optional<List<Doenca>> findByNomeIn(List<String> nomes);
}
