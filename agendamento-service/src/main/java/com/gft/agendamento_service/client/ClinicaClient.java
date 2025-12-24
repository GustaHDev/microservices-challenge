package com.gft.agendamento_service.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.gft.agendamento_service.dtos.ConsultaRequest;

@FeignClient(name = "clinica-service", url = "http://clinica-service:8082")
public interface ClinicaClient {

    @PostMapping("/api/clinica/cadastro/consulta")
    UUID createConsulta(@RequestBody ConsultaRequest consulta);

}
