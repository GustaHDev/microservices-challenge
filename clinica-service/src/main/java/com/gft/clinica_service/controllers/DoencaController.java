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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.gft.clinica_service.models.Doenca;
import com.gft.clinica_service.services.DoencaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/clinica")
@Validated
public class DoencaController {

    private final DoencaService doencaService;

    DoencaController(DoencaService doencaService) {
        this.doencaService = doencaService;
    }

    @Operation(summary = "Cadastra uma doença", description = "Cadastra uma nova doença")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Doença criada"),
            @ApiResponse(responseCode = "400", description = "Erro de validação"),
            @ApiResponse(responseCode = "500", description = "Erro inesperado")
    })
    @PostMapping("/cadastro/doenca")
    public ResponseEntity<Void> createDoenca(@RequestBody @Valid Doenca doenca) {
        doencaService.createDoenca(doenca);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(doenca.getId()).toUri();

        return ResponseEntity.created(uri).build();
    }

    @Operation(summary = "Lista doenças", description = "Lista todas as doenças")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Ok"),
            @ApiResponse(responseCode = "500", description = "Erro inesperado")
    })
    @GetMapping("/doencas")
    public ResponseEntity<List<Doenca>> findDoenca() {
        List<Doenca> doencas = doencaService.findAll();

        return ResponseEntity.ok().body(doencas);
    }

    @Operation(summary = "Encontra uma doença", description = "Encontra uma doença pelo id")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Ok"),
            @ApiResponse(responseCode = "404", description = "Doença não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro inesperado")
    })
    @GetMapping("/doenca/{id}")
    public ResponseEntity<Doenca> findDoenca(@PathVariable UUID id) {
        Doenca doenca = doencaService.findDoencaById(id);

        return ResponseEntity.ok().body(doenca);
    }

    @Operation(summary = "Lista doenças", description = "Lista doenças pelo nome")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Ok"),
            @ApiResponse(responseCode = "404", description = "Nome não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro inesperado")
    })
    @GetMapping("/doencas/{nome}")
    public ResponseEntity<List<Doenca>> findDoencasByNome(@PathVariable List<String> nome) {
        List<Doenca> doencas = doencaService.findDoencaByName(nome);

        return ResponseEntity.ok().body(doencas);
    }

    @Operation(summary = "Atualiza uma doença", description = "Atualiza os sintomas de uma doença pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Doença atualizada"),
            @ApiResponse(responseCode = "400", description = "Erro de validação"),
            @ApiResponse(responseCode = "404", description = "Doença não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro inesperado")
    })
    @PutMapping("/doenca/{id}/sintomas")
    public ResponseEntity<Doenca> setSintomas(@PathVariable UUID id, @RequestBody List<String> nomesSintomas) {
        Doenca doenca = this.doencaService.setSintomas(id, nomesSintomas);

        return ResponseEntity.ok().body(doenca);
    }

    @Operation(summary = "Atualiza uma doença", description = "Atualiza os dados de uma doença pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Doença atualizada"),
            @ApiResponse(responseCode = "400", description = "Erro de validação"),
            @ApiResponse(responseCode = "404", description = "Doença não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro inesperado")
    })
    @PutMapping("/cadastro/doenca/{id}")
    public ResponseEntity<Void> updateDoenca(@PathVariable UUID id, @RequestBody Doenca newDoenca) {
        newDoenca.setId(id);
        this.doencaService.updateDoenca(newDoenca, id);

        return ResponseEntity.noContent().build();
    }

    
    @Operation(summary = "Deleta uma doença", description = "Verifica se doença existe e deleta")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Doença deletada"),
            @ApiResponse(responseCode = "404", description = "Doença não encontrada"),
    })
    @DeleteMapping("/doenca/{id}")
    public ResponseEntity<Void> deleteDoenca(@PathVariable UUID id) {
        this.doencaService.deleteDoenca(id);

        return ResponseEntity.noContent().build();
    }

}
