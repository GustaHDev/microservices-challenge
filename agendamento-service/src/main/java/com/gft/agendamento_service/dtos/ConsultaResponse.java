package com.gft.agendamento_service.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

public record ConsultaResponse(
    String especialidadeMed,
    String nomePaciente,
    LocalDateTime dataHora,
    UUID codigo
) {}
