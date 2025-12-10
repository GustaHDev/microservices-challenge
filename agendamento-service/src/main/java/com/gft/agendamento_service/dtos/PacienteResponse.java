package com.gft.agendamento_service.dtos;

public record PacienteResponse(
    String nome,
    Integer idade,
    String sexo
) {}
