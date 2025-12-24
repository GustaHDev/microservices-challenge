package com.gft.agendamento_service.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gft.agendamento_service.models.ConsultaAgendada;

public interface ConsultaRepository extends JpaRepository<ConsultaAgendada, UUID> {
    Optional<List<ConsultaAgendada>> findByPacienteCpf(String cpf);

    boolean existsByPacienteCpfAndDataHora(String cpf, LocalDateTime dataHora);

    boolean existsByEspecialidadeMedAndDataHora(String tEspecialidadeMed, LocalDateTime dataHora);
}
