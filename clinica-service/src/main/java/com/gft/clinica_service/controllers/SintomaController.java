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

import com.gft.clinica_service.models.Sintoma;
import com.gft.clinica_service.services.SintomaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/clinica")
@Validated
public class SintomaController {

    private final SintomaService sintomaService;

    SintomaController(SintomaService sintomaService) {
        this.sintomaService = sintomaService;
    }

    @Operation(summary = "Cadastra um sintoma", description = "Cadastra um novo sintoma")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sintoma criado"),
            @ApiResponse(responseCode = "400", description = "Erro de validação"),
            @ApiResponse(responseCode = "500", description = "Erro inesperado")
    })
    @PostMapping("/cadastro/sintoma")
    public ResponseEntity<Void> createSintoma(@RequestBody @Valid Sintoma sintoma) {
        sintomaService.createSintoma(sintoma);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(sintoma.getId()).toUri();

        return ResponseEntity.created(uri).build();
    }

    @Operation(summary = "Lista sintomas", description = "Lista todos os sintomas")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Ok"),
            @ApiResponse(responseCode = "500", description = "Erro inesperado")
    })
    @GetMapping("/sintomas")
    public ResponseEntity<List<Sintoma>> findSintoma() {
        List<Sintoma> sintomas = sintomaService.findAll();

        return ResponseEntity.ok().body(sintomas);
    }

    @Operation(summary = "Encontra sintoma", description = "Encontra um sintoma pelo id")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Ok"),
            @ApiResponse(responseCode = "404", description = "Sintoma não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro inesperado")
    })
    @GetMapping("/sintoma/{id}")
    public ResponseEntity<Sintoma> findSintoma(@PathVariable UUID id) {
        Sintoma sintoma = sintomaService.findSintomaById(id);

        return ResponseEntity.ok().body(sintoma);
    }

    @Operation(summary = "Lista sintomas", description = "Lista sintomas pelo nome")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Ok"),
            @ApiResponse(responseCode = "404", description = "Nome não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro inesperado")
    })
    @GetMapping("/sintomas/{nome}")
    public ResponseEntity<List<Sintoma>> findSintomasByNome(@PathVariable List<String> nome) {
        List<Sintoma> sintomas = sintomaService.findSintomaByName(nome);

        return ResponseEntity.ok().body(sintomas);
    }

    @Operation(summary = "Atualiza um sintoma", description = "Atualiza as doenças de um sintoma pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Sintoma atualizado"),
            @ApiResponse(responseCode = "400", description = "Erro de validação"),
            @ApiResponse(responseCode = "404", description = "Sintoma não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro inesperado")
    })
    @PutMapping("/sintoma/{id}/doencas")
    public ResponseEntity<Sintoma> setDoencas(@PathVariable UUID id, @RequestBody List<String> nomesDoencas) {
        Sintoma sintoma = this.sintomaService.setDoencas(id, nomesDoencas);

        return ResponseEntity.ok().body(sintoma);
    }

    @Operation(summary = "Atualiza um sintoma", description = "Atualiza os dados de um sintoma pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Sintoma atualizado"),
            @ApiResponse(responseCode = "400", description = "Erro de validação"),
            @ApiResponse(responseCode = "404", description = "Sintoma não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro inesperado")
    })
    @PutMapping("/cadastro/sintoma/{id}")
    public ResponseEntity<Void> updateSintoma(@PathVariable UUID id, @RequestBody Sintoma newSintoma) {
        newSintoma.setId(id);
        this.sintomaService.updateSintoma(newSintoma, id);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Deleta um sintoma", description = "Verifica se sintoma existe e deleta")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Sintoma deletado"),
            @ApiResponse(responseCode = "404", description = "Sintoma não encontrado"),
    })
    @DeleteMapping("/sintoma/{id}")
    public ResponseEntity<Void> deleteSintoma(@PathVariable UUID id) {
        this.sintomaService.deleteSintoma(id);

        return ResponseEntity.noContent().build();
    }

}
