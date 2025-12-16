package com.gft.clinica_service.client;

import java.util.UUID;

import com.gft.clinica_service.dtos.ProcedimentoRequest;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "procedimento-service", url = "http://procedimento-service:8083")
public interface ProcedimentoClient {

    @PostMapping("/api/cadastro/procedimento")
    UUID createProcedimento(@RequestBody ProcedimentoRequest request);

}
