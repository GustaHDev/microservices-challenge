package com.gft.agendamento_service.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.gft.agendamento_service.client.ClinicaClient;
import com.gft.agendamento_service.dtos.ConsultaRequest;
import com.gft.agendamento_service.dtos.MessageResponse;
import com.gft.agendamento_service.exceptions.ApiIntegrationException;
import com.gft.agendamento_service.exceptions.BusinessException;
import com.gft.agendamento_service.exceptions.ResourceNotFoundException;
import com.gft.agendamento_service.models.ConsultaAgendada;
import com.gft.agendamento_service.models.Paciente;
import com.gft.agendamento_service.models.Status;
import com.gft.agendamento_service.publishers.ConsultaPublisher;
import com.gft.agendamento_service.repositories.ConsultaRepository;
import com.gft.agendamento_service.repositories.PacienteRepository;

@Service
public class ConsultaService {
    private final ConsultaRepository consultaRepository;
    private final PacienteRepository pacienteRepository;

    private final ConsultaPublisher consultaPublisher;

    private final ClinicaClient clinicaClient;

    ConsultaService(ConsultaRepository consultaRepository,
            PacienteRepository pacienteRepository,
            ConsultaPublisher consultaPublisher,
            ClinicaClient clinicaClient) {
        this.consultaRepository = consultaRepository;
        this.pacienteRepository = pacienteRepository;
        this.consultaPublisher = consultaPublisher;
        this.clinicaClient = clinicaClient;
    }

    public MessageResponse createConsulta(ConsultaAgendada consulta) {
        Optional<Paciente> paciente = pacienteRepository.findByCpf(consulta.getPaciente().getCpf());
        if (!paciente.isPresent()) {
            throw new ResourceNotFoundException("Paciente não encontrado");
        }

        boolean consultaExistsByPacienteCpf = this.consultaRepository
                .existsByPacienteCpfAndDataHora(paciente.get().getCpf(), consulta.getDataHora());
        boolean consultaExistsByEspecialidadeMed = this.consultaRepository
                .existsByEspecialidadeMedAndDataHora(consulta.getEspecialidadeMed(), consulta.getDataHora());

        if (consultaExistsByPacienteCpf) {
            throw new BusinessException("Já existe uma consulta para este paciente no horário solicitado.",
                    HttpStatus.CONFLICT);
        }
        if (consultaExistsByEspecialidadeMed) {
            throw new BusinessException("Já existe uma consulta para esta especialidade no horário solicitado.",
                    HttpStatus.CONFLICT);
        }

        ConsultaAgendada newConsulta = new ConsultaAgendada();
        newConsulta.setPaciente(paciente.get());
        newConsulta.setDataHora(consulta.getDataHora());
        newConsulta.setEspecialidadeMed(consulta.getEspecialidadeMed());
        newConsulta.setStatus(Status.AGUARDANDO_CONFIRMACAO);

        ConsultaAgendada savedConsulta = this.consultaRepository.save(newConsulta);

        ConsultaRequest request = new ConsultaRequest();
        request.setCodigoAgendamento(savedConsulta.getId());
        request.setCpfPaciente(paciente.get().getCpf());
        request.setEspecialidadeMed(consulta.getEspecialidadeMed());
        request.setDataHora(consulta.getDataHora());

        try {
            UUID codigoConsulta = this.clinicaClient.createConsulta(request);
            savedConsulta.setStatus(Status.AGENDADO);

            this.consultaRepository.save(savedConsulta);

            MessageResponse consultaResponse = new MessageResponse();
            consultaResponse.setMessage("a consulta de " + paciente.get().getNome() + " foi marcada com sucesso para "
                    + newConsulta.getEspecialidadeMed() + " na data " + newConsulta.getDataHora());
            consultaResponse.setCodigo(codigoConsulta);

            return consultaResponse;
        } catch (Exception e) {
            throw new ApiIntegrationException("Erro ao agendar consulta no serviço da clínica: " + e.getMessage(), e);
        }

    }

    public List<ConsultaAgendada> findAll() {
        return this.consultaRepository.findAll();
    }

    public ConsultaAgendada findConsultaById(UUID id) {
        Optional<ConsultaAgendada> consulta = this.consultaRepository.findById(id);

        return consulta.orElseThrow(() -> new ResourceNotFoundException("Consulta não encontrada. ID: " + id));
    }

    public List<ConsultaAgendada> findByPacienteCpf(String cpf) {
        Optional<List<ConsultaAgendada>> consultas = this.consultaRepository.findByPacienteCpf(cpf);

        return consultas
                .orElseThrow(() -> new ResourceNotFoundException("Nenhuma consulta foi encontrada. CPF: " + cpf));
    }

    public ConsultaAgendada updateConsulta(ConsultaAgendada newConsulta, UUID id) {
        ConsultaAgendada updatedConsulta = this.findConsultaById(id);

        boolean consultaExistsByPacienteCpf = this.consultaRepository
                .existsByPacienteCpfAndDataHora(newConsulta.getPaciente().getCpf(), newConsulta.getDataHora());
        boolean consultaExistsByEspecialidadeMed = this.consultaRepository
                .existsByEspecialidadeMedAndDataHora(newConsulta.getEspecialidadeMed(), newConsulta.getDataHora());

        if (consultaExistsByPacienteCpf) {
            throw new BusinessException("Já existe uma consulta para este paciente no horário solicitado.",
                    HttpStatus.CONFLICT);
        }
        if (consultaExistsByEspecialidadeMed) {
            throw new BusinessException("Já existe uma consulta para esta especialidade no horário solicitado.",
                    HttpStatus.CONFLICT);
        }

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
