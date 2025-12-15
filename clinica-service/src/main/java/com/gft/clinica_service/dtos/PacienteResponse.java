package com.gft.clinica_service.dtos;
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
public class PacienteResponse {

    private UUID id;

    private String nome;

    private String cpf;

    private Integer idade;
    
    private String sexo;

}
