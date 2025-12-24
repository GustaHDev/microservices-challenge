package com.gft.clinica_service.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.gft.clinica_service.exceptions.ResourceNotFoundException;
import com.gft.clinica_service.models.Medico;
import com.gft.clinica_service.repositories.MedicoRepository;

@Service
public class MedicoService {

    private final MedicoRepository medicoRepository;

    MedicoService(MedicoRepository medicoRepository) {
        this.medicoRepository = medicoRepository;
    }

    public Medico createMedico(Medico medico) {
        Medico newMedico = this.medicoRepository.save(medico);

        return newMedico;
    }

    public List<Medico> findAll() {
        return this.medicoRepository.findAll();
    }

    public Medico findMedicoById(UUID id) {
        Optional<Medico> medico = this.medicoRepository.findById(id);

        return medico.orElseThrow(() -> new ResourceNotFoundException("Médico não encontrado. ID: " + id));
    }

    public List<Medico> findMedicoByEspecialidade(String especialidade) {
        List<Medico> medico = this.medicoRepository.findByEspecialidade(especialidade);

        if (medico.isEmpty()) {
            throw new ResourceNotFoundException("Médico não encontrado. Especialidade: " + especialidade);
        }

        return medico;
    }

    public Medico updateMedico(Medico newMedico, UUID id) {
        Medico updatedMedico = this.findMedicoById(id);

        updatedMedico.setNome(newMedico.getNome());
        updatedMedico.setCrm(newMedico.getCrm());
        updatedMedico.setEspecialidade(newMedico.getEspecialidade());

        return this.medicoRepository.save(updatedMedico);
    }

    public void deleteMedico(UUID id) {
        this.findMedicoById(id);
        
        this.medicoRepository.deleteById(id);
    }

}
