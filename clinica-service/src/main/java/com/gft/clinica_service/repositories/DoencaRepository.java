package com.gft.clinica_service.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.gft.clinica_service.models.Doenca;


public interface DoencaRepository extends JpaRepository<Doenca, UUID> {
    @Query("SELECT DISTINCT d FROM Doenca d JOIN d.sintomas s WHERE s IN :sintomas")
    Optional<List<Doenca>> findBySintomas(List<String> sintomas);

    Optional<List<Doenca>> findByNomeIn(List<String> nomes);
}
