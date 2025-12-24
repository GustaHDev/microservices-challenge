package com.gft.clinica_service.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AgendaRequest {

    private UUID codigoAgendamento;

    private String cpfPaciente;

    private LocalDateTime dataHora;

    private String especialidadeMed;

}
