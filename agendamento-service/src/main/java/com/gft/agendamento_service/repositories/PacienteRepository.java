package com.gft.agendamento_service.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gft.agendamento_service.models.Paciente;


public interface PacienteRepository extends JpaRepository<Paciente, UUID> {
    Optional<Paciente> findByCpf(String cpf);

    Optional<List<Paciente>> findByName(String nome);
}
