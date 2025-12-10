package com.gft.agendamento_service.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.gft.agendamento_service.dtos.ConsultaResponse;
import com.gft.agendamento_service.models.ConsultaAgendada;
import com.gft.agendamento_service.models.Paciente;
import com.gft.agendamento_service.models.Status;
import com.gft.agendamento_service.repositories.ConsultaRepository;

@Service
public class ConsultaService {
    private final ConsultaRepository consultaRepository;
    private final PacienteService pacienteService;

    ConsultaService(ConsultaRepository consultaRepository, PacienteService pacienteService) {
        this.consultaRepository = consultaRepository;
        this.pacienteService = pacienteService;
    }

    private ConsultaResponse toConsultaResponse(ConsultaAgendada consultaAgendada) {

        ConsultaResponse consultaResponse = new ConsultaResponse(
                consultaAgendada.getEspecialidadeMed(),
                consultaAgendada.getPaciente().getNome(),
                consultaAgendada.getDataHora(),
                consultaAgendada.getId());

        return consultaResponse;
    }

    public ConsultaResponse createConsulta(ConsultaAgendada consulta) {
        Paciente paciente = pacienteService.findByCpf(consulta.getPaciente().getCpf());

        ConsultaAgendada newConsulta = new ConsultaAgendada();

        newConsulta.setPaciente(paciente);
        newConsulta.setDataHora(consulta.getDataHora());
        newConsulta.setEspecialidadeMed(consulta.getEspecialidadeMed());
        newConsulta.setStatus(Status.AGUARDANDO_CONFIRMACAO);

        this.consultaRepository.save(newConsulta);

        ConsultaResponse consultaResponse = toConsultaResponse(newConsulta);

        return consultaResponse;
    }

    public List<ConsultaAgendada> findAll() {
        return this.consultaRepository.findAll();
    }

    public ConsultaAgendada findConsultaById(UUID id) {
        Optional<ConsultaAgendada> consulta = this.consultaRepository.findById(id);

        return consulta.orElse(null);
    }

    public ConsultaResponse findResponseById(UUID id) {
        ConsultaAgendada consulta = this.findConsultaById(id);
        ConsultaResponse consultaResponse = toConsultaResponse(consulta);

        return consultaResponse;
    }

    public List<ConsultaAgendada> findByPacienteCpf(String cpf) {
        Optional<List<ConsultaAgendada>> consultas = this.consultaRepository.findByPacienteCpf(cpf);

        return consultas.orElse(null);
    }

    public ConsultaAgendada updateConsulta(ConsultaAgendada newConsulta, UUID id) {
        ConsultaAgendada updatedConsulta = this.findConsultaById(id);

        updatedConsulta.setPaciente(newConsulta.getPaciente());
        updatedConsulta.setDataHora(newConsulta.getDataHora());
        updatedConsulta.setEspecialidadeMed(newConsulta.getEspecialidadeMed());
        updatedConsulta.setStatus(Status.AGUARDANDO_CONFIRMACAO);

        return this.consultaRepository.save(updatedConsulta);
    }

    public void deleteConsulta(UUID id) {
        this.findConsultaById(id);

        this.consultaRepository.deleteById(id);
    }

}
