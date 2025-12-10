package com.gft.agendamento_service.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gft.agendamento_service.models.ProcedimentoAgendado;

public interface ProcedimentoRepository extends JpaRepository<ProcedimentoAgendado, UUID> {
    Optional<List<ProcedimentoAgendado>> findByPacienteCpf(String cpf);
}
