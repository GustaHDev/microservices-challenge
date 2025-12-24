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

    @PostMapping("/cadastro/procedimento")
    public ResponseEntity<UUID> saveProcedimento(@RequestBody Procedimento procedimento,
                                                 @RequestHeader("X-Request-Origin") String origem
    ) {
        OrigemProcedimento origemEnum = OrigemProcedimento.valueOf(origem);
        this.procedimentoService.saveProcedimento(procedimento, origemEnum);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(procedimento.getId()).toUri();

        return ResponseEntity.created(uri).body(procedimento.getId());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Procedimento> findProcedimentoById(@PathVariable UUID id) {
        Procedimento procedimento = this.procedimentoService.findById(id);

        return ResponseEntity.ok().body(procedimento);
    }

    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<Procedimento> findProcedimentoByPacienteCpf(@PathVariable String cpf) {
        Procedimento procedimento = this.procedimentoService.findByPacienteCpf(cpf);

        return ResponseEntity.ok().body(procedimento);
    }

    @GetMapping("/{dataHora}")
    public ResponseEntity<List<Procedimento>> findProcedimentoByHorario(@PathVariable LocalDateTime dataHora) {
        List<Procedimento> procedimentos = this.procedimentoService.findByDataHora(dataHora);

        return ResponseEntity.ok().body(procedimentos);
    }
    
    @GetMapping("/procedimentos")
    public ResponseEntity<List<Procedimento>> findAll() {
        List<Procedimento> procedimentos = this.procedimentoService.findAll();

        return ResponseEntity.ok().body(procedimentos);
    }

    @PutMapping("/procedimentos/marcar")
    public ResponseEntity<Void> marcarProcedimento(@RequestBody ExameRequest request
    ) {
        this.procedimentoService.marcarProcedimento(request);
        
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProcedimento(@PathVariable UUID id) {
        Procedimento procedimento = this.procedimentoService.findById(id);

        this.procedimentoService.deleteProcedimento(procedimento.getId());
        return ResponseEntity.noContent().build();
    }
 
}
