package com.gft.clinica_service.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gft.clinica_service.models.Doenca;
import com.gft.clinica_service.models.Sintoma;

public interface SintomaRepository extends JpaRepository<Sintoma, UUID> {
    Optional<List<Sintoma>> findByDoencas(List<Doenca> doencas);

    Optional<List<Sintoma>> findByNomeIn(List<String> nomes);
}
