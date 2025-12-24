package com.gft.procedimento_service.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gft.procedimento_service.models.Procedimento;
import java.util.List;
import java.time.LocalDateTime;


public interface ProcedimentoRepository extends JpaRepository<Procedimento, UUID> {
    Optional<List<Procedimento>> findByDataHora(LocalDateTime dataHora);

    Optional<Procedimento> findByPacienteCpf(String pacienteCpf);

    Optional<Procedimento> existsByTipoProcedimentoAndDataHora(String tipoProcedimento, LocalDateTime dataHora);
}
