package com.gft.agendamento_service.controllers;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.gft.agendamento_service.dtos.PacienteResponse;
import com.gft.agendamento_service.models.Paciente;
import com.gft.agendamento_service.models.Paciente.CreatePaciente;
import com.gft.agendamento_service.models.Paciente.UpdatePaciente;
import com.gft.agendamento_service.services.PacienteService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/api")
@Validated
public class PacienteController {

    private final PacienteService pacienteService;

    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    @PostMapping("/cadastro/paciente")
    public ResponseEntity<Void> createPaciente(@Validated(CreatePaciente.class) @RequestBody Paciente paciente) {
        this.pacienteService.createPaciente(paciente);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(paciente.getId()).toUri();

        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/pacientes")
    public ResponseEntity<List<Paciente>> getPacientes() {
        List<Paciente> pacientes = this.pacienteService.findAll();

        return ResponseEntity.ok().body(pacientes);
    }

    @GetMapping("/paciente/{id}")
    public ResponseEntity<PacienteResponse> getPaciente(@PathVariable UUID id) {
        PacienteResponse pacienteResponse = this.pacienteService.findResponseById(id);

        return ResponseEntity.ok().body(pacienteResponse);
    }

    @PutMapping("/paciente/{id}")
    public ResponseEntity<Void> updatePaciente(@Validated(UpdatePaciente.class) @PathVariable UUID id, @RequestBody Paciente paciente) {
        paciente.setId(id);
        this.pacienteService.updatePaciente(paciente, id);
        
        return ResponseEntity.noContent().build();
    }
    
    @DeleteMapping("/paciente/{id}")
    public ResponseEntity<Void> deletePaciente(@PathVariable UUID id) {
        this.pacienteService.deletePaciente(id);
        return ResponseEntity.noContent().build();
    }

}
