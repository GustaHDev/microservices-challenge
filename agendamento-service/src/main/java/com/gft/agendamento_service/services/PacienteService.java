package com.gft.agendamento_service.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.gft.agendamento_service.models.Paciente;
import com.gft.agendamento_service.repositories.PacienteRepository;

@Service
public class PacienteService {

    private final PacienteRepository pacienteRepository;

    PacienteService(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    public Paciente create(Paciente paciente) {
        Paciente newPaciente = pacienteRepository.save(paciente);

        return newPaciente;
    }

    public Paciente update(Paciente newPaciente, UUID id) {
        Paciente updatedPaciente = pacienteRepository.findById(id)
                .map(p -> {
                    p.setCpf(newPaciente.getCpf());
                    p.setNome(newPaciente.getNome());
                    p.setIdade(newPaciente.getIdade());
                    p.setSexo(newPaciente.getSexo());
                    return pacienteRepository.save(p);
                }).orElse(null);
        return updatedPaciente;
    }

    public List<Paciente> findAll() {
        return pacienteRepository.findAll();
    }

    public Paciente findById(UUID id) {
        return pacienteRepository.findById(id).orElse(null);
    }

    public Paciente findByCpf(String cpf) {
        return pacienteRepository.findByCpf(cpf);
    }

    public Boolean delete(UUID id) {
        if (pacienteRepository.existsById(id)) {
            pacienteRepository.deleteById(id);
            return true;
        }
        return false;
    }

}
