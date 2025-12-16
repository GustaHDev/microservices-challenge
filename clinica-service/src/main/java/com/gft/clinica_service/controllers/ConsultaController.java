package com.gft.clinica_service.controllers;

import java.net.URI;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.gft.clinica_service.dtos.AgendaRequest;
import com.gft.clinica_service.dtos.ConsultaRequest;
import com.gft.clinica_service.dtos.MessageResponse;
import com.gft.clinica_service.dtos.ProcedimentoRequest;
import com.gft.clinica_service.models.Consulta;
import com.gft.clinica_service.services.ConsultaService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api")
@Validated
public class ConsultaController {

    private final ConsultaService consultaService;

    ConsultaController(ConsultaService consultaService) {
        this.consultaService = consultaService;
    }

    @PostMapping("/cadastro/consulta")
    public ResponseEntity<UUID> createConsulta(@Valid @RequestBody AgendaRequest request) {
        Consulta consulta = consultaService.createConsulta(request);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(consulta.getId()).toUri();

        return ResponseEntity.created(uri).body(consulta.getId());
    }

    @PutMapping("clinica/AtenderConsulta")
    public ResponseEntity<Void> updateConsulta(@RequestBody ConsultaRequest request) {
        this.consultaService.updateConsulta(request);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/clinica/cadastro/procedimento")
    public ResponseEntity<MessageResponse> cadastrarExameAltaComplexidade(@RequestBody ProcedimentoRequest request) {
        MessageResponse response = this.consultaService.setExameAltaComplexidade(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
