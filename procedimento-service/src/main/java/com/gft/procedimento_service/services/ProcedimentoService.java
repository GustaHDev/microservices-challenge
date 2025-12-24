package com.gft.procedimento_service.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.gft.procedimento_service.dtos.ExameRequest;
import com.gft.procedimento_service.exceptions.BusinessException;
import com.gft.procedimento_service.exceptions.ResourceNotFoundException;
import com.gft.procedimento_service.models.Complexidade;
import com.gft.procedimento_service.models.OrigemProcedimento;
import com.gft.procedimento_service.models.Procedimento;
import com.gft.procedimento_service.models.Status;
import com.gft.procedimento_service.models.TipoExame;
import com.gft.procedimento_service.publishers.ProcedimentoPublisher;
import com.gft.procedimento_service.repositories.ProcedimentoRepository;

@Service
public class ProcedimentoService {

    private final ProcedimentoRepository procedimentoRepository;

    private final ProcedimentoPublisher procedimentoPublisher;

    ProcedimentoService(ProcedimentoRepository procedimentoRepository,
                        ProcedimentoPublisher procedimentoPublisher
    ) {
        this.procedimentoRepository = procedimentoRepository;
        this.procedimentoPublisher = procedimentoPublisher;
    }

    public Complexidade definirComplexidade(String tipoExame) {
        TipoExame exame = TipoExame.fromNome(tipoExame.toUpperCase());
        return exame.getComplexidade();
    }

    public Procedimento saveProcedimento(Procedimento procedimento, OrigemProcedimento origem) {

        procedimento.setComplexidade(this.definirComplexidade(procedimento.getTipoProcedimento()));

        if (procedimento.getComplexidade() == Complexidade.ALTA && origem == OrigemProcedimento.AGENDAMENTO) {
            throw new BusinessException("Procedimentos de complexidade ALTA só podem ser marcados na clínica.", HttpStatus.BAD_REQUEST);
        }

        return this.procedimentoRepository.save(procedimento);
    }

    public Procedimento findById(UUID id) {
        Optional <Procedimento> procedimento = this.procedimentoRepository.findById(id);

        return procedimento.orElseThrow(() -> new ResourceNotFoundException("Procedimento não encontrado. ID: " + id));
    }

    public Procedimento findByPacienteCpf(String cpf) {
        Optional<Procedimento> procedimento = this.procedimentoRepository.findByPacienteCpf(cpf);
        
        return procedimento.orElseThrow(() -> new ResourceNotFoundException("Procedimento não encontrado. CPF: " + cpf));
    }

    public List<Procedimento> findByDataHora(LocalDateTime dataHora) {
        Optional<List<Procedimento>> procedimentos = this.procedimentoRepository.findByDataHora(dataHora);

        return procedimentos.orElseThrow(() -> new ResourceNotFoundException("Nenhum procedimento foi encontrado. Horário: " + dataHora));
    }

    public List<Procedimento> findAll() {
        return this.procedimentoRepository.findAll();
    }

    public Procedimento marcarProcedimento(ExameRequest request) {
        Procedimento procedimento = this.findById(request.getIdExame());

        if (procedimento != null &&
                procedimento.getDataHora().equals(request.getDataHora()) &&
                procedimento.getPacienteCpf().equals(request.getCpfPaciente())) {
            procedimento.setStatus(Status.FINALIZADO);
            this.procedimentoPublisher.publishProcedimentoFinalizado(procedimento.getCodigoAgendamento());
            return this.procedimentoRepository.save(procedimento);
        } else {
            throw new IllegalArgumentException("Procedimento não encontrado ou dados inválidos.");
        }
    }

    public Procedimento updateProcedimento(Procedimento newProcedimento, UUID id, OrigemProcedimento origem) {
        Procedimento updatedProcedimento = this.findById(id);

        if (updatedProcedimento != null) {
            updatedProcedimento.setTipoProcedimento(newProcedimento.getTipoProcedimento());
            updatedProcedimento.setDataHora(newProcedimento.getDataHora());
            updatedProcedimento.setPacienteCpf(newProcedimento.getPacienteCpf());
            updatedProcedimento.setStatus(newProcedimento.getStatus());

            return this.saveProcedimento(updatedProcedimento, origem);
        } else {
            throw new IllegalArgumentException("Procedimento não encontrado.");
        }
    }

    public void cancelarProcedimento(UUID procedimentoId) {
        Procedimento procedimento = this.procedimentoRepository.findByCodigoAgendamento(procedimentoId).orElseThrow(() -> new ResourceNotFoundException("Procedimento não encontrado. ID: " + procedimentoId));

        procedimento.setStatus(Status.CANCELADO);

        this.procedimentoRepository.save(procedimento);
    }

    public void deleteProcedimento(UUID id) {
        Procedimento procedimento = this.findById(id);

        this.procedimentoRepository.delete(procedimento);
    }

}
