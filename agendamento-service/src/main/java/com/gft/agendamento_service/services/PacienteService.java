package com.gft.agendamento_service.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.gft.agendamento_service.dtos.PacienteResponse;
import com.gft.agendamento_service.models.Paciente;
import com.gft.agendamento_service.repositories.PacienteRepository;

@Service
public class PacienteService {

    private final PacienteRepository pacienteRepository;

    PacienteService(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    private PacienteResponse toPacienteResponse(Paciente paciente) {
        
        PacienteResponse pacienteResponse = new PacienteResponse(
            paciente.getNome(),
            paciente.getIdade(),
            paciente.getSexo()
        );

        return pacienteResponse;
    }

    public Paciente createPaciente(Paciente paciente) {
        Paciente newPaciente = this.pacienteRepository.save(paciente);

        return newPaciente;
    }

    public List<Paciente> findAll() {
        return this.pacienteRepository.findAll();
    }

    public Paciente findPacienteById(UUID id) {
        Optional<Paciente> paciente = this.pacienteRepository.findById(id);

        return paciente.orElse(null);
    }

    public PacienteResponse findResponseById(UUID id) {
        Paciente paciente = this.findPacienteById(id);
        PacienteResponse pacienteResponse = toPacienteResponse(paciente);

        return pacienteResponse;
    }

    public Paciente findByCpf(String cpf) {
        Optional<Paciente> paciente = this.pacienteRepository.findByCpf(cpf);

        return paciente.orElse(null);
    }

    public List<Paciente> findByName(String nome) {
        String nomeCase = nome.toLowerCase();
        Optional<List<Paciente>> pacientes = this.pacienteRepository.findByName(nomeCase);

        return pacientes.orElse(null);
    }

    public Paciente updatePaciente(Paciente newPaciente, UUID id) {
        Paciente updatedPaciente = this.findPacienteById(id);

        updatedPaciente.setCpf(newPaciente.getCpf());
        updatedPaciente.setNome(newPaciente.getNome());
        updatedPaciente.setIdade(newPaciente.getIdade());
        updatedPaciente.setSexo(newPaciente.getSexo());

        return this.pacienteRepository.save(updatedPaciente);
    }

    public void deletePaciente(UUID id) {
        this.findPacienteById(id);

        this.pacienteRepository.deleteById(id);
    }

}
