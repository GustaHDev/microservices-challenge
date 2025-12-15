package com.gft.clinica_service.controllers;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.gft.clinica_service.models.Medico;
import com.gft.clinica_service.services.MedicoService;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
@RequestMapping("/api")
@Validated
public class MedicoController {

    private final MedicoService medicoService;

    MedicoController(MedicoService medicoService) {
        this.medicoService = medicoService;
    }

    @PostMapping("/cadastro/medico")
    public ResponseEntity<Void> createMedico(@RequestBody @Valid Medico medico) {
        medicoService.createMedico(medico);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(medico.getId()).toUri();

        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/medicos")
    public ResponseEntity<List<Medico>> findMedicos() {
        List<Medico> medicos = medicoService.findAll();

        return ResponseEntity.ok().body(medicos);
    }

    @GetMapping("/medico/{id}")
    public ResponseEntity<Medico> findMedico(@PathVariable UUID id) {
        Medico medico = medicoService.findMedicoById(id);

        return ResponseEntity.ok().body(medico);
    }

    @PutMapping("/cadastro/medico/{id}")
    public ResponseEntity<Void> updateMedico(@PathVariable UUID id, @RequestBody Medico newMedico) {
        newMedico.setId(id);
        this.medicoService.updateMedico(newMedico, id);
        
        return ResponseEntity.noContent().build();
    }
    
    @DeleteMapping("/medico/{id}")
    public ResponseEntity<Void> deleteMedico(@PathVariable UUID id) {
        this.medicoService.deleteMedico(id);

        return ResponseEntity.noContent().build();
    }

}
