package com.gft.agendamento_service.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.gft.agendamento_service.client.ClinicaClient;
import com.gft.agendamento_service.dtos.ConsultaRequest;
import com.gft.agendamento_service.dtos.MessageResponse;
import com.gft.agendamento_service.models.ConsultaAgendada;
import com.gft.agendamento_service.models.Paciente;
import com.gft.agendamento_service.models.Status;
import com.gft.agendamento_service.publishers.ConsultaPublisher;
import com.gft.agendamento_service.repositories.ConsultaRepository;

@Service
public class ConsultaService {
    private final ConsultaRepository consultaRepository;

    private final PacienteService pacienteService;

    private final ConsultaPublisher consultaPublisher;

    private final ClinicaClient clinicaClient;

    ConsultaService(ConsultaRepository consultaRepository, 
                    PacienteService pacienteService, 
                    ConsultaPublisher consultaPublisher,
                    ClinicaClient clinicaClient) {
        this.consultaRepository = consultaRepository;
        this.pacienteService = pacienteService;
        this.consultaPublisher = consultaPublisher;
        this.clinicaClient = clinicaClient;
    }

    public MessageResponse createConsulta(ConsultaAgendada consulta) {
        Paciente paciente = pacienteService.findByCpf(consulta.getPaciente().getCpf());

        
        ConsultaRequest request = new ConsultaRequest(
            paciente.getCpf(),
            consulta.getEspecialidadeMed(),
            consulta.getDataHora()
        );
        
        ConsultaAgendada newConsulta = new ConsultaAgendada();
        newConsulta.setStatus(Status.AGUARDANDO_CONFIRMACAO);

        UUID codigoConsulta = this.clinicaClient.createConsulta(request);

        newConsulta.setStatus(Status.AGENDADO);
        newConsulta.setPaciente(paciente);
        newConsulta.setDataHora(consulta.getDataHora());
        newConsulta.setEspecialidadeMed(consulta.getEspecialidadeMed());

        this.consultaRepository.save(newConsulta);

        MessageResponse consultaResponse = new MessageResponse();
        consultaResponse.setMessage("a consulta de " + paciente.getNome() + " foi marcada com sucesso para " + newConsulta.getEspecialidadeMed() + " na data " + newConsulta.getDataHora());
        consultaResponse.setCodigo(codigoConsulta);


        return consultaResponse;
    }

    public List<ConsultaAgendada> findAll() {
        return this.consultaRepository.findAll();
    }

    public ConsultaAgendada findConsultaById(UUID id) {
        Optional<ConsultaAgendada> consulta = this.consultaRepository.findById(id);

        return consulta.orElse(null);
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

    public void finalizarConsulta(UUID consultaId) {
        ConsultaAgendada consulta = this.findConsultaById(consultaId);

        consulta.setStatus(Status.FINALIZADO);

        this.consultaRepository.save(consulta);
    }

    public void deleteConsulta(UUID id) {
        this.findConsultaById(id);

        this.consultaPublisher.publishConsultaCancelada(id);
        this.consultaRepository.deleteById(id);
    }

}
