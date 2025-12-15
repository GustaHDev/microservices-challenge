package com.gft.clinica_service.dtos;

import com.gft.clinica_service.models.Status;
import java.time.LocalDateTime;
import java.util.List;

import com.gft.clinica_service.models.Complexidade;

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
public class ConsultaDTO {

    private String crmMedico;

    private String nomePaciente;

    private LocalDateTime dataHora;

    private List<String> sintomas;

    private Status status;

    private Complexidade complexidade;

}
