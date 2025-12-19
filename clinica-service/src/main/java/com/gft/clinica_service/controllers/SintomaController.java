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

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/clinica")
@Validated
public class SintomaController {

    private final SintomaService sintomaService;

    SintomaController(SintomaService sintomaService) {
        this.sintomaService = sintomaService;
    }

    @PostMapping("/cadastro/sintoma")
    public ResponseEntity<Void> createSintoma(@RequestBody @Valid Sintoma sintoma) {
        sintomaService.createSintoma(sintoma);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(sintoma.getId()).toUri();

        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/sintomas")
    public ResponseEntity<List<Sintoma>> findSintoma() {
        List<Sintoma> sintomas = sintomaService.findAll();

        return ResponseEntity.ok().body(sintomas);
    }

    @GetMapping("/sintoma/{id}")
    public ResponseEntity<Sintoma> findSintoma(@PathVariable UUID id) {
        Sintoma sintoma = sintomaService.findSintomaById(id);

        return ResponseEntity.ok().body(sintoma);
    }

    @GetMapping("/sintomas/{nome}")
    public ResponseEntity<List<Sintoma>> findSintomasByNome(@PathVariable List<String> nome) {
        List<Sintoma> sintomas = sintomaService.findSintomaByName(nome);

        return ResponseEntity.ok().body(sintomas);
    }

    @PutMapping("/sintoma/{id}/doencas")
    public ResponseEntity<Sintoma> setDoencas(@PathVariable UUID id, @RequestBody List<String> nomesDoencas) {
        Sintoma sintoma = this.sintomaService.setDoencas(id, nomesDoencas);
        
        return ResponseEntity.ok().body(sintoma);
    }

    @PutMapping("/cadastro/sintoma/{id}")
    public ResponseEntity<Void> updateSintoma(@PathVariable UUID id, @RequestBody Sintoma newSintoma) {
        newSintoma.setId(id);
        this.sintomaService.updateSintoma(newSintoma, id);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/sintoma/{id}")
    public ResponseEntity<Void> deleteSintoma(@PathVariable UUID id) {
        this.sintomaService.deleteSintoma(id);

        return ResponseEntity.noContent().build();
    }

}
