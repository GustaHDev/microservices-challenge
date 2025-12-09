package com.gft.agendamento_service.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gft.agendamento_service.models.Paciente;


public interface PacienteRepository extends JpaRepository<Paciente, UUID> {
    Paciente findByCpf(String cpf);
}
