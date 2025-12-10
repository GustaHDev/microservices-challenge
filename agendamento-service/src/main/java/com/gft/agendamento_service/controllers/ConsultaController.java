package com.gft.agendamento_service.controllers;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.gft.agendamento_service.models.ConsultaAgendada;
import com.gft.agendamento_service.models.ConsultaAgendada.CreateConsulta;
import com.gft.agendamento_service.models.ConsultaAgendada.UpdateConsulta;
import com.gft.agendamento_service.services.ConsultaService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public ResponseEntity<Void> createConsulta(
            @Validated(CreateConsulta.class) @RequestBody ConsultaAgendada consulta) {
        this.consultaService.createConsulta(consulta);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(consulta.getId()).toUri();

        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/consultas/{cpf}")
    public ResponseEntity<List<ConsultaAgendada>> getConsultas(@PathVariable String cpf) {
        List<ConsultaAgendada> consultas = this.consultaService.findByPacienteCpf(cpf);

        return ResponseEntity.ok().body(consultas);
    }

    @PutMapping("/cadastro/consulta/{id}")
    public ResponseEntity<Void> updateConsulta(@Validated(UpdateConsulta.class) @PathVariable UUID id, @RequestBody ConsultaAgendada newConsulta) {
        newConsulta.setId(id);
        this.consultaService.updateConsulta(newConsulta, id);
        
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/consulta/{id}")
    public ResponseEntity<Void> deleteConsulta(@PathVariable UUID id) {
        this.consultaService.deleteConsulta(id);
        return ResponseEntity.noContent().build();
    }

}
