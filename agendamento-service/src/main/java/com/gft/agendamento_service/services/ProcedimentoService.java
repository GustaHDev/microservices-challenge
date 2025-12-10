package com.gft.agendamento_service.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.gft.agendamento_service.dtos.ProcedimentoResponse;
import com.gft.agendamento_service.models.Paciente;
import com.gft.agendamento_service.models.ProcedimentoAgendado;
import com.gft.agendamento_service.models.Status;
import com.gft.agendamento_service.repositories.ProcedimentoRepository;

@Service
public class ProcedimentoService {
private final ProcedimentoRepository procedimentoRepository;
private final PacienteService pacienteService;

    ProcedimentoService(ProcedimentoRepository procedimentoRepository, PacienteService pacienteService) {
        this.procedimentoRepository = procedimentoRepository;
        this.pacienteService = pacienteService;
    }

    private ProcedimentoResponse toProcedimentoResponse(ProcedimentoAgendado procedimentoAgendado) {

        ProcedimentoResponse procedimentoResponse = new ProcedimentoResponse(
                procedimentoAgendado.getTipoExame(),
                procedimentoAgendado.getPaciente().getNome(),
                procedimentoAgendado.getDataHora(),
                procedimentoAgendado.getId());

        return procedimentoResponse;
    }

    public ProcedimentoResponse createProcedimento(ProcedimentoAgendado procedimento) {
        Paciente paciente = pacienteService.findByCpf(procedimento.getPaciente().getCpf());
    
        ProcedimentoAgendado newProcedimento = new ProcedimentoAgendado();

        newProcedimento.setPaciente(paciente);
        newProcedimento.setDataHora(procedimento.getDataHora());
        newProcedimento.setTipoExame(procedimento.getTipoExame());
        newProcedimento.setStatus(Status.AGUARDANDO_CONFIRMACAO);

        this.procedimentoRepository.save(newProcedimento);
        ProcedimentoResponse procedimentoResponse = toProcedimentoResponse(newProcedimento);

        return procedimentoResponse;
    }

    public List<ProcedimentoAgendado> findAll() {
        return this.procedimentoRepository.findAll();
    }

    public ProcedimentoAgendado findProcedimentoById(UUID id) {
        Optional<ProcedimentoAgendado> procedimento = this.procedimentoRepository.findById(id);

        return procedimento.orElse(null);
    }

    public ProcedimentoResponse findResponseById(UUID id) {
        ProcedimentoAgendado procedimento = this.findProcedimentoById(id);
        ProcedimentoResponse procedimentoResponse = toProcedimentoResponse(procedimento);

        return procedimentoResponse;
    }

    public List<ProcedimentoAgendado> findByPacienteCpf(String cpf) {
        Optional<List<ProcedimentoAgendado>> procedimentos = this.procedimentoRepository.findByPacienteCpf(cpf);

        return procedimentos.orElse(null);
    }

    public ProcedimentoAgendado updateProcedimento(ProcedimentoAgendado newProcedimento, UUID id) {
        ProcedimentoAgendado updatedProcedimento = this.findProcedimentoById(id);

        updatedProcedimento.setPaciente(newProcedimento.getPaciente());
        updatedProcedimento.setDataHora(newProcedimento.getDataHora());
        updatedProcedimento.setTipoExame(newProcedimento.getTipoExame());
        updatedProcedimento.setStatus(Status.AGUARDANDO_CONFIRMACAO);

        return this.procedimentoRepository.save(updatedProcedimento);
    }

    public void deleteProcedimento(UUID id) {
        this.findProcedimentoById(id);

        this.procedimentoRepository.deleteById(id);
    }
}
