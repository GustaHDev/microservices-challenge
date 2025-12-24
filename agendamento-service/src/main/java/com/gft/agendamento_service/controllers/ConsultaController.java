package com.gft.agendamento_service.controllers;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.gft.agendamento_service.dtos.MessageResponse;
import com.gft.agendamento_service.models.ConsultaAgendada;
import com.gft.agendamento_service.models.ConsultaAgendada.CreateConsulta;
import com.gft.agendamento_service.models.ConsultaAgendada.UpdateConsulta;
import com.gft.agendamento_service.services.ConsultaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/agendamento")
@Validated
public class ConsultaController {

    private final ConsultaService consultaService;

    ConsultaController(ConsultaService consultaService) {
        this.consultaService = consultaService;
    }

    @Operation(summary = "Agenda uma consulta", description = "Valida conflitos, envia para clínica criar consulta e retorna ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Consulta criada"),
            @ApiResponse(responseCode = "400", description = "Erro de validação"),
            @ApiResponse(responseCode = "409", description = "Horário já ocupado"),
            @ApiResponse(responseCode = "503", description = "Erro na comunicação com a clínica"),
            @ApiResponse(responseCode = "500", description = "Erro inesperado")
    })
    @PostMapping("/cadastro/consulta")
    public ResponseEntity<MessageResponse> createConsulta(
            @Validated(CreateConsulta.class) @RequestBody ConsultaAgendada consulta) {
        MessageResponse response = this.consultaService.createConsulta(consulta);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(consulta.getId()).toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @Operation(summary = "Lista consultas", description = "Lista todas as consultas associadas a um CPF")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Ok"),
            @ApiResponse(responseCode = "404", description = "Cpf não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro inesperado")
    })
    @GetMapping("/consultas/{cpf}")
    public ResponseEntity<List<ConsultaAgendada>> getConsultas(@PathVariable String cpf) {
        List<ConsultaAgendada> consultas = this.consultaService.findByPacienteCpf(cpf);

        return ResponseEntity.ok().body(consultas);
    }

    @Operation(summary = "Atualiza uma consulta", description = "Atualiza os dados de uma consulta pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Consulta atualizada"),
            @ApiResponse(responseCode = "400", description = "Erro de validação"),
            @ApiResponse(responseCode = "404", description = "ID não encontrado"),
            @ApiResponse(responseCode = "409", description = "Horário já ocupado"),
            @ApiResponse(responseCode = "500", description = "Erro inesperado")
    })
    @PutMapping("/cadastro/consulta/{id}")
    public ResponseEntity<Void> updateConsulta(@Validated(UpdateConsulta.class) @PathVariable UUID id,
            @RequestBody ConsultaAgendada newConsulta) {
        newConsulta.setId(id);
        this.consultaService.updateConsulta(newConsulta, id);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Deleta uma consulta", description = "Verifica se consulta existe, deleta e envia mensagem para serviço da clínica para mudar status para CANCELADO")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Consulta deletada"),
            @ApiResponse(responseCode = "404", description = "Consulta não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro inesperado")
    })
    @DeleteMapping("/consulta/{id}")
    public ResponseEntity<Void> deleteConsulta(@PathVariable UUID id) {
        this.consultaService.deleteConsulta(id);
        return ResponseEntity.noContent().build();
    }

}
