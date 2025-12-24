package com.gft.procedimento_service.controllers;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.gft.procedimento_service.dtos.ExameRequest;
import com.gft.procedimento_service.models.OrigemProcedimento;
import com.gft.procedimento_service.models.Procedimento;
import com.gft.procedimento_service.services.ProcedimentoService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/procedimento")
@Validated
public class ProcedimentoController {
    private final ProcedimentoService procedimentoService;

    ProcedimentoController(ProcedimentoService procedimentoService) {
        this.procedimentoService = procedimentoService;
    }

    @Operation(summary = "Cadastra um procedimento", description = "Cadastra um novo procedimento ao ser chamado pelo serviço de agendamento, valida a origem e a complexidade")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Procedimento criado"),
            @ApiResponse(responseCode = "400", description = "Erro de validação"),
            @ApiResponse(responseCode = "500", description = "Erro inesperado")
    })
    @PostMapping("/cadastro/procedimento")
    public ResponseEntity<UUID> saveProcedimento(@RequestBody Procedimento procedimento,
            @RequestHeader("X-Request-Origin") String origem) {
        OrigemProcedimento origemEnum = OrigemProcedimento.valueOf(origem);
        this.procedimentoService.saveProcedimento(procedimento, origemEnum);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(procedimento.getId()).toUri();

        return ResponseEntity.created(uri).body(procedimento.getId());
    }

    @Operation(summary = "Encontra procedimento", description = "Encontra um procedimento pelo id")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Ok"),
            @ApiResponse(responseCode = "404", description = "Procedimento não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro inesperado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Procedimento> findProcedimentoById(@PathVariable UUID id) {
        Procedimento procedimento = this.procedimentoService.findById(id);

        return ResponseEntity.ok().body(procedimento);
    }

    @Operation(summary = "Lista procedimentos", description = "Lista todos os procedimentos ligados a um cpf")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Ok"),
            @ApiResponse(responseCode = "404", description = "Procedimento não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro inesperado")
    })
    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<Procedimento> findProcedimentoByPacienteCpf(@PathVariable String cpf) {
        Procedimento procedimento = this.procedimentoService.findByPacienteCpf(cpf);

        return ResponseEntity.ok().body(procedimento);
    }

    @Operation(summary = "Lista procedimentos", description = "Lista todos os procedimentos pelo horario")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Ok"),
            @ApiResponse(responseCode = "404", description = "Procedimento não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro inesperado")
    })
    @Operation(summary = "Lista procedimentos", description = "Lista todos os procedimentos pelo horario")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Ok"),
            @ApiResponse(responseCode = "404", description = "Procedimento não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro inesperado")
    })
    @GetMapping("/{dataHora}")
    public ResponseEntity<List<Procedimento>> findProcedimentoByHorario(@PathVariable LocalDateTime dataHora) {
        List<Procedimento> procedimentos = this.procedimentoService.findByDataHora(dataHora);

        return ResponseEntity.ok().body(procedimentos);
    }

    @Operation(summary = "Lista procedimentos", description = "Lista todos os procedimentos")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Ok"),
            @ApiResponse(responseCode = "500", description = "Erro inesperado")
    })
    @GetMapping("/procedimentos")
    public ResponseEntity<List<Procedimento>> findAll() {
        List<Procedimento> procedimentos = this.procedimentoService.findAll();

        return ResponseEntity.ok().body(procedimentos);
    }

    @Operation(summary = "Marca um procedimento", description = "Encontra procedimento com base no id, valida os dados, atualiza procedimento e envia mensagem para serviço de agendamento para mudar status para FINALIZADO")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Consulta criada"),
            @ApiResponse(responseCode = "400", description = "Erro de validação"),
            @ApiResponse(responseCode = "404", description = "Procedimento não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro inesperado")
        })
        @PutMapping("/procedimentos/marcar")
        public ResponseEntity<Void> marcarProcedimento(@RequestBody ExameRequest request) {
        this.procedimentoService.marcarProcedimento(request);

        return ResponseEntity.noContent().build();
    }

        @Operation(summary = "Deleta um procedimento", description = "Verifica se procedimento existe e deleta")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Procedimento deletado"),
            @ApiResponse(responseCode = "404", description = "Procedimento não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro inesperado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProcedimento(@PathVariable UUID id) {
        Procedimento procedimento = this.procedimentoService.findById(id);

        this.procedimentoService.deleteProcedimento(procedimento.getId());
        return ResponseEntity.noContent().build();
    }

}
