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

@RestController
@RequestMapping("/api/agendamento")
@Validated
public class ProcedimentoController {

    private final ProcedimentoService procedimentoService;

    ProcedimentoController(ProcedimentoService procedimentoService) {
        this.procedimentoService = procedimentoService;
    }

    @PostMapping("/cadastro/procedimento")
    public ResponseEntity<MessageResponse> createProcedimento(
            @Validated(CreateProcedimento.class) @RequestBody ProcedimentoAgendado procedimento) {
        MessageResponse response =this.procedimentoService.createProcedimento(procedimento);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(procedimento.getId()).toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping("/procedimentos/{cpf}")
    public ResponseEntity<List<ProcedimentoAgendado>> getProcedimentos(@PathVariable String cpf) {
        List<ProcedimentoAgendado> procedimentos = this.procedimentoService.findByPacienteCpf(cpf);

        return ResponseEntity.ok().body(procedimentos);
    }

    @PutMapping("/cadastro/procedimento/{id}")
    public ResponseEntity<Void> updateProcedimento(@Validated(UpdateProcedimento.class) @PathVariable UUID id,
            @RequestBody ProcedimentoAgendado newProcedimento) {
        newProcedimento.setId(id);
        this.procedimentoService.updateProcedimento(newProcedimento, id);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/procedimento/{id}")
    public ResponseEntity<Void> deleteProcedimento(@PathVariable UUID id) {
        this.procedimentoService.deleteProcedimento(id);
        return ResponseEntity.noContent().build();
    }

}
