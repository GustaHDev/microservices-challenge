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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/agendamento")
@Validated
public class PacienteController {

    private final PacienteService pacienteService;

    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    @Operation(summary = "Cadastro de paciente", description = "Valida os dados e cadastra um novo paciente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Paciente criado"),
            @ApiResponse(responseCode = "400", description = "Erro de validação"),
            @ApiResponse(responseCode = "500", description = "Erro inesperado")
    })
    @PostMapping("/cadastro/paciente")
    public ResponseEntity<Void> createPaciente(@Validated(CreatePaciente.class) @RequestBody Paciente paciente) {
        this.pacienteService.createPaciente(paciente);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(paciente.getId()).toUri();

        return ResponseEntity.created(uri).build();
    }

    @Operation(summary = "Lista pacientes", description = "Lista todos os pacientes cadastrados")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Ok"),
            @ApiResponse(responseCode = "500", description = "Erro inesperado")
        })
        @GetMapping("/pacientes")
        public ResponseEntity<List<Paciente>> getPacientes() {
        List<Paciente> pacientes = this.pacienteService.findAll();

        return ResponseEntity.ok().body(pacientes);
    }

    @Operation(summary = "Encontra paciente", description = "Encontra um paciente pelo id")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Ok"),
            @ApiResponse(responseCode = "404", description = "Id não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro inesperado")
    })
    @GetMapping("/paciente/{id}")
    public ResponseEntity<PacienteResponse> getPaciente(@PathVariable UUID id) {
        PacienteResponse pacienteResponse = this.pacienteService.findResponseById(id);
        
        return ResponseEntity.ok().body(pacienteResponse);
    }
    
    @Operation(summary = "Encontra paciente", description = "Encontra um paciente pelo cpf")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Ok"),
            @ApiResponse(responseCode = "404", description = "Cpf não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro inesperado")
    })
    @GetMapping("/paciente/cpf/{cpf}")
    public ResponseEntity<Paciente> getPacienteByCpf(@PathVariable String cpf) {
        Paciente paciente = this.pacienteService.findByCpf(cpf);
        
        return ResponseEntity.ok().body(paciente);
    }
    
    @Operation(summary = "Lista pacientes", description = "Encontra pacientes pelo nome")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Ok"),
            @ApiResponse(responseCode = "500", description = "Erro inesperado")
        })
        @GetMapping("/paciente/{nome}")
    public ResponseEntity<List<Paciente>> getPacienteByName(@PathVariable String nome) {
        List<Paciente> pacientes = this.pacienteService.findByName(nome);

        return ResponseEntity.ok().body(pacientes);
    }

        @Operation(summary = "Atualiza um paciente", description = "Atualiza os dados de um paciente pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Paciente atualizado"),
            @ApiResponse(responseCode = "400", description = "Erro de validação"),
            @ApiResponse(responseCode = "404", description = "ID não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro inesperado")
    })
    @PutMapping("/paciente/{id}")
    public ResponseEntity<Void> updatePaciente(@Validated(UpdatePaciente.class) @PathVariable UUID id,
            @RequestBody Paciente paciente) {
        paciente.setId(id);
        this.pacienteService.updatePaciente(paciente, id);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Deleta um paciente", description = "Verifica se paciente existe e deleta")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Paciente deletado"),
            @ApiResponse(responseCode = "404", description = "Paciente não encontrado"),
    })
    @DeleteMapping("/paciente/{id}")
    public ResponseEntity<Void> deletePaciente(@PathVariable UUID id) {
        this.pacienteService.deletePaciente(id);
        return ResponseEntity.noContent().build();
    }

}
