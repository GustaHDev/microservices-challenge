package com.gft.clinica_service.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gft.clinica_service.models.Consulta;
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;


public interface ConsultaRepository extends JpaRepository<Consulta, UUID> {
    List<Consulta> findByDataHora(LocalDateTime dataHora);

    Optional<Consulta> findByCodigoAgendamento(UUID codigoAgendamento);

    List<Consulta> findByCrmMedico(String crmMedico);

    List<Consulta> findByCpfPaciente(String cpfPaciente);
}
