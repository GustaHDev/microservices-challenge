package com.gft.clinica_service.dtos;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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
public class ConsultaRequest {

    @NotBlank(message = "O cpf do paciente é obrigatório")
    private String cpfPaciente;

    private UUID codigoConsulta;

    private LocalDateTime horario;

    @NotNull(message = "Os sintomas são obrigatórios")
    private List<String> sintomas;

}
