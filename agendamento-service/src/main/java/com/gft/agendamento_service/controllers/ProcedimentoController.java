package com.gft.agendamento_service.controllers;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.gft.agendamento_service.dtos.MessageResponse;
import com.gft.agendamento_service.models.ProcedimentoAgendado;
import com.gft.agendamento_service.models.ProcedimentoAgendado.CreateProcedimento;
import com.gft.agendamento_service.models.ProcedimentoAgendado.UpdateProcedimento;
import com.gft.agendamento_service.services.ProcedimentoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/agendamento")
@Validated
public class ProcedimentoController {

    private final ProcedimentoService procedimentoService;

    ProcedimentoController(ProcedimentoService procedimentoService) {
        this.procedimentoService = procedimentoService;
    }

    @Operation(summary = "Agenda um procedimento", description = "Valida conflitos, envia para serviço de procedimentos para criar procedimento e retorna ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Procedimento criado"),
            @ApiResponse(responseCode = "400", description = "Erro de validação"),
            @ApiResponse(responseCode = "409", description = "Horário já ocupado"),
            @ApiResponse(responseCode = "503", description = "Erro na comunicação com o centro cirúrgico"),
            @ApiResponse(responseCode = "500", description = "Erro inesperado")
    })
    @PostMapping("/cadastro/procedimento")
    public ResponseEntity<MessageResponse> createProcedimento(
            @Validated(CreateProcedimento.class) @RequestBody ProcedimentoAgendado procedimento) {
        MessageResponse response = this.procedimentoService.createProcedimento(procedimento);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(procedimento.getId()).toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @Operation(summary = "Lista procedimentos", description = "Lista todos os procedimentos associados a um CPF")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Ok"),
            @ApiResponse(responseCode = "404", description = "Cpf não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro inesperado")
    })
    @GetMapping("/procedimentos/{cpf}")
    public ResponseEntity<List<ProcedimentoAgendado>> getProcedimentos(@PathVariable String cpf) {
        List<ProcedimentoAgendado> procedimentos = this.procedimentoService.findByPacienteCpf(cpf);

        return ResponseEntity.ok().body(procedimentos);
    }

    @Operation(summary = "Atualiza um procedimento", description = "Atualiza os dados de um procedimento pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Procedimento atualizado"),
            @ApiResponse(responseCode = "400", description = "Erro de validação"),
            @ApiResponse(responseCode = "404", description = "ID não encontrado"),
            @ApiResponse(responseCode = "409", description = "Horário já ocupado"),
            @ApiResponse(responseCode = "500", description = "Erro inesperado")
    })
    @PutMapping("/cadastro/procedimento/{id}")
    public ResponseEntity<Void> updateProcedimento(@Validated(UpdateProcedimento.class) @PathVariable UUID id,
            @RequestBody ProcedimentoAgendado newProcedimento) {
        newProcedimento.setId(id);
        this.procedimentoService.updateProcedimento(newProcedimento, id);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Deleta um procedimento", description = "Verifica se procedimento existe, deleta e envia mensagem para serviço de procedimentos para mudar status para CANCELADO")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Procedimento deletado"),
            @ApiResponse(responseCode = "404", description = "Procedimento não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro inesperado")
        })
        @DeleteMapping("/procedimento/{id}")
        public ResponseEntity<Void> deleteProcedimento(@PathVariable UUID id) {
            this.procedimentoService.deleteProcedimento(id);
            return ResponseEntity.noContent().build();
        }
        
}
