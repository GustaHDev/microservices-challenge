package com.gft.clinica_service.dtos;

import com.gft.clinica_service.models.Prioridade;

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

    private String cpfPaciente;

    private String tipoProcedimento;

    private Prioridade prioridade;

}
