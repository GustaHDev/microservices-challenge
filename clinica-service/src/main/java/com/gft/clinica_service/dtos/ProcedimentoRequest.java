package com.gft.clinica_service.dtos;

import com.gft.clinica_service.models.Prioridade;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProcedimentoRequest {

    @NotNull(message = "cpf do paciente é obrigatório")
    private String cpfPaciente;

    @NotNull(message = "o tipo do procedimento é obrigatório")
    private String tipoProcedimento;

    @NotNull(message = "a prioridade do procedimento é obrigatório")
    private Prioridade prioridade;

}
