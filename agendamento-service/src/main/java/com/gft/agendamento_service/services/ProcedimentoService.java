package com.gft.agendamento_service.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.gft.agendamento_service.client.ProcedimentoClient;
import com.gft.agendamento_service.dtos.MessageResponse;
import com.gft.agendamento_service.dtos.ProcedimentoRequest;
import com.gft.agendamento_service.exceptions.ResourceNotFoundException;
import com.gft.agendamento_service.models.Paciente;
import com.gft.agendamento_service.models.ProcedimentoAgendado;
import com.gft.agendamento_service.models.Status;
import com.gft.agendamento_service.publishers.ProcedimentoPublisher;
import com.gft.agendamento_service.repositories.PacienteRepository;
import com.gft.agendamento_service.repositories.ProcedimentoRepository;

@Service
public class ProcedimentoService {
private final ProcedimentoRepository procedimentoRepository;

private final PacienteRepository pacienteRepository;

private final ProcedimentoPublisher procedimentoPublisher;

private final ProcedimentoClient procedimentoClient;

    ProcedimentoService(ProcedimentoRepository procedimentoRepository, 
                        PacienteRepository pacienteRepository,
                        ProcedimentoPublisher procedimentoPublisher,
                        ProcedimentoClient procedimentoClient) {
        this.procedimentoRepository = procedimentoRepository;
        this.pacienteRepository = pacienteRepository;
        this.procedimentoPublisher = procedimentoPublisher;
        this.procedimentoClient = procedimentoClient;
    }

    public MessageResponse createProcedimento(ProcedimentoAgendado procedimento) {
        Optional<Paciente> paciente = pacienteRepository.findByCpf(procedimento.getPaciente().getCpf());
        if (!paciente.isPresent()) {
            throw new ResourceNotFoundException("Paciente não encontrado");
        }
    
        ProcedimentoRequest request = new ProcedimentoRequest(
            paciente.get().getCpf(),
            procedimento.getTipoExame(),
            procedimento.getDataHora()
        );

        ProcedimentoAgendado newProcedimento = new ProcedimentoAgendado();
        newProcedimento.setStatus(Status.AGUARDANDO_CONFIRMACAO);
        
        UUID codigoExame = this.procedimentoClient.createProcedimento(request);

        newProcedimento.setStatus(Status.AGENDADO);
        newProcedimento.setPaciente(paciente.get());
        newProcedimento.setDataHora(procedimento.getDataHora());
        newProcedimento.setTipoExame(procedimento.getTipoExame());

        this.procedimentoRepository.save(newProcedimento);
        MessageResponse procedimentoResponse = new MessageResponse();
        procedimentoResponse.setMessage("O exame " + procedimento.getTipoExame() + " de " + paciente.get().getNome() + " foi marcado com sucesso para a data " + procedimento.getDataHora()); 
        procedimentoResponse.setCodigo(codigoExame);

        return procedimentoResponse;
    }

    public List<ProcedimentoAgendado> findAll() {
        return this.procedimentoRepository.findAll();
    }

    public ProcedimentoAgendado findProcedimentoById(UUID id) {
        Optional<ProcedimentoAgendado> procedimento = this.procedimentoRepository.findById(id);

        return procedimento.orElseThrow(() -> new ResourceNotFoundException("Procedimento não encontrado. ID: " + id));
    }

    public List<ProcedimentoAgendado> findByPacienteCpf(String cpf) {
        Optional<List<ProcedimentoAgendado>> procedimentos = this.procedimentoRepository.findByPacienteCpf(cpf);

        return procedimentos.orElseThrow(() -> new ResourceNotFoundException("Nenhuma consulta foi encontrada. CPF: " + cpf));
    }

    public ProcedimentoAgendado updateProcedimento(ProcedimentoAgendado newProcedimento, UUID id) {
        ProcedimentoAgendado updatedProcedimento = this.findProcedimentoById(id);

        updatedProcedimento.setPaciente(newProcedimento.getPaciente());
        updatedProcedimento.setDataHora(newProcedimento.getDataHora());
        updatedProcedimento.setTipoExame(newProcedimento.getTipoExame());
        updatedProcedimento.setStatus(Status.AGUARDANDO_CONFIRMACAO);

        return this.procedimentoRepository.save(updatedProcedimento);
    }

    public void finalizarProcedimento(UUID procedimentoId) {
        ProcedimentoAgendado procedimento = this.findProcedimentoById(procedimentoId);

        procedimento.setStatus(Status.FINALIZADO);

        this.procedimentoRepository.save(procedimento);
    }

    public void deleteProcedimento(UUID id) {
        this.findProcedimentoById(id);

        this.procedimentoPublisher.publishProcedimentoCancelado(id);
        this.procedimentoRepository.deleteById(id);
    }
}
