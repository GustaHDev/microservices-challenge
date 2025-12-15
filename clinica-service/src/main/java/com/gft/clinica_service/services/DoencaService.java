package com.gft.clinica_service.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.gft.clinica_service.models.Doenca;
import com.gft.clinica_service.models.Sintoma;
import com.gft.clinica_service.repositories.DoencaRepository;
import com.gft.clinica_service.repositories.SintomaRepository;

@Service
public class DoencaService {

    private final DoencaRepository doencaRepository;
    private final SintomaRepository sintomaRepository;

    DoencaService(DoencaRepository doencaRepository,
                  SintomaRepository sintomaRepository
    ) {
        this.doencaRepository = doencaRepository;
        this.sintomaRepository = sintomaRepository;
    }

    public Doenca createDoenca(Doenca doenca) {
        Doenca newDoenca = this.doencaRepository.save(doenca);

        return newDoenca;
    }

    public List<Doenca> findAll() {
        return this.doencaRepository.findAll();
    }

    public Doenca findDoencaById(UUID id) {
        Optional<Doenca> doenca = this.doencaRepository.findById(id);

        return doenca.orElse(null);
    }

    public List<Doenca> findDoencaBySintomas(List<Sintoma> sintomas) {
        Optional<List<Doenca>> doenca = this.doencaRepository.findBySintomas(sintomas);

        return doenca.orElse(null);
    }

    public List<Doenca> findDoencaByName(List<String> nomes) {
        Optional<List<Doenca>> doencas = this.doencaRepository.findByNomeIn(nomes);

        return doencas.orElse(null);
    }

    public Doenca setSintomas(UUID id, List<String> nomesSintomas) {
        Doenca doenca = this.findDoencaById(id);
        Optional<List<Sintoma>> sintomas = this.sintomaRepository.findByNomeIn(nomesSintomas);

        doenca.setSintomas(sintomas.get().stream().map(Sintoma::getNome).toList());
        return this.doencaRepository.save(doenca);
    }

    public Doenca updateDoenca(Doenca newDoenca, UUID id) {
        Doenca updatedDoenca = this.findDoencaById(id);

        updatedDoenca.setNome(newDoenca.getNome());
        updatedDoenca.setSintomas(newDoenca.getSintomas());
        updatedDoenca.setTratamentos(newDoenca.getTratamentos());

        return this.doencaRepository.save(updatedDoenca);
    }

    public void deleteDoenca(UUID id) {
        this.findDoencaById(id);

        this.doencaRepository.deleteById(id);
    }

}
