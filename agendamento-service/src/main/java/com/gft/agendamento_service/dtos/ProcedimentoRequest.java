package com.gft.agendamento_service.dtos;

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
public class ProcedimentoRequest {

    private UUID codigoAgendamento;

    private String pacienteCpf;

    private String tipoProcedimento;

    private LocalDateTime dataHora;

}
