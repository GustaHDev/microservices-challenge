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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Cadastra uma consulta", description = "É chamado pelo serviço de agendamento, valida dados, e retorna ID para agendamento")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Consulta criada"),
            @ApiResponse(responseCode = "400", description = "Erro de validação"),
            @ApiResponse(responseCode = "500", description = "Erro inesperado")
    })
    @PostMapping("/cadastro/consulta")
    public ResponseEntity<UUID> createConsulta(@Valid @RequestBody AgendaRequest request) {
        Consulta consulta = consultaService.createConsulta(request);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(consulta.getId()).toUri();

        return ResponseEntity.created(uri).body(consulta.getId());
    }

    @Operation(summary = "Atende uma consulta", description = "Atualiza os dados de uma consulta pelo código enviado no serviço de agendamento ou pelo horario")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Consulta atualizada"),
            @ApiResponse(responseCode = "400", description = "Erro de validação"),
            @ApiResponse(responseCode = "404", description = "doenças ou sintomas não encontrados"),
            @ApiResponse(responseCode = "503", description = "Erro de integração com serviço de agendamento"),
            @ApiResponse(responseCode = "500", description = "Erro inesperado")
    })
    @PutMapping("/AtenderConsulta")
    public ResponseEntity<ConsultaResponse> updateConsulta(@RequestBody ConsultaRequest request) {
        ConsultaResponse response = this.consultaService.updateConsulta(request);

        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "Cadastra um procedimento", description = "Cadastra procedimentos de alta complexidade")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Procedimento criada"),
            @ApiResponse(responseCode = "400", description = "Erro de validação"),
            @ApiResponse(responseCode = "503", description = "Erro de integração com serviço de procedimentos"),
            @ApiResponse(responseCode = "500", description = "Erro inesperado")
        })
        @PostMapping("/cadastro/procedimento")
    public ResponseEntity<MessageResponse> cadastrarExameAltaComplexidade(@RequestBody ProcedimentoRequest request) {
        MessageResponse response = this.consultaService.setExameAltaComplexidade(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Lista consultas", description = "Lista todas as consultas")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Ok"),
        @ApiResponse(responseCode = "500", description = "Erro inesperado")
    })
    @GetMapping("/consultas")
    public ResponseEntity<List<ConsultaDTO>> findConsultas() {
        List<ConsultaDTO> consultas = this.consultaService.findConsultas();

        return ResponseEntity.ok().body(consultas);
    }

}
