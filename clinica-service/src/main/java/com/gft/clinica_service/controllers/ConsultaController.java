package com.gft.clinica_service.controllers;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.gft.clinica_service.dtos.AgendaRequest;
import com.gft.clinica_service.dtos.ConsultaDTO;
import com.gft.clinica_service.dtos.ConsultaRequest;
import com.gft.clinica_service.dtos.ConsultaResponse;
import com.gft.clinica_service.dtos.MessageResponse;
import com.gft.clinica_service.dtos.ProcedimentoRequest;
import com.gft.clinica_service.models.Consulta;
import com.gft.clinica_service.services.ConsultaService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/clinica")
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

    @PutMapping("/AtenderConsulta")
    public ResponseEntity<ConsultaResponse> updateConsulta(@RequestBody ConsultaRequest request) {
        ConsultaResponse response = this.consultaService.updateConsulta(request);

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/cadastro/procedimento")
    public ResponseEntity<MessageResponse> cadastrarExameAltaComplexidade(@RequestBody ProcedimentoRequest request) {
        MessageResponse response = this.consultaService.setExameAltaComplexidade(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/consultas")
    public ResponseEntity<List<ConsultaDTO>> findConsultas() {
        List<ConsultaDTO> consultas = this.consultaService.findConsultas();

        return ResponseEntity.ok().body(consultas);
    }
    

}
