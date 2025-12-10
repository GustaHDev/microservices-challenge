package com.gft.agendamento_service.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

public record ProcedimentoResponse(
    String tipoExame,
    String nomePaciente,
    LocalDateTime dataHora,
    UUID codigo
) {

}
