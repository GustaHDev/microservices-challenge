package com.gft.clinica_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.gft.clinica_service.dtos.PacienteResponse;

@FeignClient(name = "agendamento-service", url = "http://agendamento-service:8081")
public interface AgendamentoClient {

    @GetMapping("/api/paciente/cpf/{cpf}")
    PacienteResponse getPacienteByCpf(@PathVariable String cpf);

    @GetMapping("/api/paciente/{nome}")
    PacienteResponse getPacienteByNome(@PathVariable String nome);

}
