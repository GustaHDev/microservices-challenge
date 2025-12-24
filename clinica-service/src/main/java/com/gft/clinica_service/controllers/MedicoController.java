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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/clinica")
@Validated
public class MedicoController {

    private final MedicoService medicoService;

    MedicoController(MedicoService medicoService) {
        this.medicoService = medicoService;
    }

    @Operation(summary = "Cadastra um médico", description = "Cadastra um novo médico")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Médico criado"),
            @ApiResponse(responseCode = "400", description = "Erro de validação"),
            @ApiResponse(responseCode = "500", description = "Erro inesperado")
    })
    @PostMapping("/cadastro/medico")
    public ResponseEntity<Void> createMedico(@RequestBody @Valid Medico medico) {
        medicoService.createMedico(medico);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(medico.getId()).toUri();

        return ResponseEntity.created(uri).build();
    }

    @Operation(summary = "Lista médicos", description = "Lista todos os médicos")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Ok"),
            @ApiResponse(responseCode = "500", description = "Erro inesperado")
    })
    @GetMapping("/medicos")
    public ResponseEntity<List<Medico>> findMedicos() {
        List<Medico> medicos = medicoService.findAll();

        return ResponseEntity.ok().body(medicos);
    }

    @Operation(summary = "Encontra médico", description = "Encontra um médico pelo id")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Ok"),
            @ApiResponse(responseCode = "404", description = "Médico não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro inesperado")
    })
    @GetMapping("/medico/{id}")
    public ResponseEntity<Medico> findMedico(@PathVariable UUID id) {
        Medico medico = medicoService.findMedicoById(id);

        return ResponseEntity.ok().body(medico);
    }

    @Operation(summary = "Atualiza um médico", description = "Atualiza os dados de um médico pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Médico atualizado"),
            @ApiResponse(responseCode = "400", description = "Erro de validação"),
            @ApiResponse(responseCode = "404", description = "Médico não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro inesperado")
    })
    @PutMapping("/cadastro/medico/{id}")
    public ResponseEntity<Void> updateMedico(@PathVariable UUID id, @RequestBody Medico newMedico) {
        newMedico.setId(id);
        this.medicoService.updateMedico(newMedico, id);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Deleta um médico", description = "Verifica se médico existe e deleta")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Médico deletado"),
            @ApiResponse(responseCode = "404", description = "Médico não encontrado"),
    })
    @DeleteMapping("/medico/{id}")
    public ResponseEntity<Void> deleteMedico(@PathVariable UUID id) {
        this.medicoService.deleteMedico(id);

        return ResponseEntity.noContent().build();
    }

}
