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

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
@Validated
public class DoencaController {

    private final DoencaService doencaService;

    DoencaController(DoencaService doencaService) {
        this.doencaService = doencaService;
    }

    @PostMapping("/cadastro/doenca")
    public ResponseEntity<Void> createDoenca(@RequestBody @Valid Doenca doenca) {
        doencaService.createDoenca(doenca);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(doenca.getId()).toUri();

        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/doencas")
    public ResponseEntity<List<Doenca>> findDoenca() {
        List<Doenca> doencas = doencaService.findAll();

        return ResponseEntity.ok().body(doencas);
    }

    @GetMapping("/doenca/{id}")
    public ResponseEntity<Doenca> findDoenca(@PathVariable UUID id) {
        Doenca doenca = doencaService.findDoencaById(id);

        return ResponseEntity.ok().body(doenca);
    }

    @GetMapping("/doencas/{nome}")
    public ResponseEntity<List<Doenca>> findDoencasByNome(@PathVariable List<String> nome) {
        List<Doenca> doencas = doencaService.findDoencaByName(nome);

        return ResponseEntity.ok().body(doencas);
    }

    @PutMapping("/doenca/{id}/sintomas")
    public ResponseEntity<Doenca> setSintomas(@PathVariable UUID id, @RequestBody List<String> nomesSintomas) {
        Doenca doenca = this.doencaService.setSintomas(id, nomesSintomas);

        return ResponseEntity.ok().body(doenca);
    }

    @PutMapping("/cadastro/doenca/{id}")
    public ResponseEntity<Void> updateDoenca(@PathVariable UUID id, @RequestBody Doenca newDoenca) {
        newDoenca.setId(id);
        this.doencaService.updateDoenca(newDoenca, id);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/doenca/{id}")
    public ResponseEntity<Void> deleteDoenca(@PathVariable UUID id) {
        this.doencaService.deleteDoenca(id);

        return ResponseEntity.noContent().build();
    }

}
