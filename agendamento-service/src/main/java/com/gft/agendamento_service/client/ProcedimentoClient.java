package com.gft.agendamento_service.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.gft.agendamento_service.dtos.ProcedimentoRequest;

@FeignClient(name = "procedimento-service", url = "http://procedimento-service:8083")
public interface ProcedimentoClient {

    @PostMapping("/api/procedimento/cadastro/procedimento")
    UUID createProcedimento(@RequestBody ProcedimentoRequest request,
                            @RequestHeader("X-Request-Origin") String origin
    );

}
