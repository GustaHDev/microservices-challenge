package com.gft.clinica_service.dtos;

import java.util.List;

import com.gft.clinica_service.models.Doenca;

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
public class ConsultaResponse {

    private List<Doenca> possiveisDoencas;

    private String message;

}
