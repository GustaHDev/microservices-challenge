package com.gft.clinica_service.dtos;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "cpf do paciente é obrigatório")
    private String cpfPaciente;

    @NotNull(message = "horario é obrigatório")
    private LocalDateTime dataHora;

    @NotBlank(message = "especialidade do médico é obrigatório")
    private String especialidadeMed;

    }
