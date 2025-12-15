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
public class SintomaService {

    private final SintomaRepository sintomaRepository;
    private final DoencaRepository doencaRepository;

    SintomaService(SintomaRepository sintomaRepository,
                   DoencaRepository doencaRepository
    ) {
        this.sintomaRepository = sintomaRepository;
        this.doencaRepository = doencaRepository;
    }

    public Sintoma createSintoma(Sintoma sintoma) {
        Sintoma newSintoma = this.sintomaRepository.save(sintoma);

        return newSintoma;
    }

    public List<Sintoma> findAll() {
        return this.sintomaRepository.findAll();
    }

    public Sintoma findSintomaById(UUID id) {
        Optional<Sintoma> sintoma = this.sintomaRepository.findById(id);

        return sintoma.orElse(null);
    }

    public List<Sintoma> findSintomaByDoencas(List<Doenca> doencas) {
        Optional<List<Sintoma>> sintomas = this.sintomaRepository.findByDoencas(doencas);

        return sintomas.orElse(null);
    }

    public List<Sintoma> findSintomaByName(List<String> nomes) {
        Optional<List<Sintoma>> sintomas = this.sintomaRepository.findByNomeIn(nomes);

        return sintomas.orElse(null);
    }

    public Sintoma setDoencas(UUID id, List<String> nomeDoencas) {
        Sintoma sintoma = this.findSintomaById(id);
        Optional<List<Doenca>> doencas = this.doencaRepository.findByNomeIn(nomeDoencas);

        sintoma.setDoencas(doencas.get().stream().map(Doenca::getNome).toList());
        return this.sintomaRepository.save(sintoma);
    }

    public Sintoma updateSintoma(Sintoma newSintoma, UUID id) {
        Sintoma updatedSintoma = this.findSintomaById(id);

        updatedSintoma.setNome(newSintoma.getNome());
        updatedSintoma.setPrioridade(newSintoma.getPrioridade());
        updatedSintoma.setDoencas(newSintoma.getDoencas());

        return this.sintomaRepository.save(updatedSintoma);
    }

    public void deleteSintoma(UUID id) {
        this.findSintomaById(id);

        this.sintomaRepository.deleteById(id);
    }

}
