package com.gft.clinica_service.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gft.clinica_service.models.Medico;

public interface MedicoRepository extends JpaRepository<Medico, UUID> {
    Optional<List<Medico>> findByEspecialidade(String especialidade);
}
