package com.gft.clinica_service.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;

import com.gft.clinica_service.client.AgendamentoClient;
import com.gft.clinica_service.client.ProcedimentoClient;
import com.gft.clinica_service.dtos.AgendaRequest;
import com.gft.clinica_service.dtos.ConsultaDTO;
import com.gft.clinica_service.dtos.ConsultaRequest;
import com.gft.clinica_service.dtos.ConsultaResponse;
import com.gft.clinica_service.dtos.ProcedimentoRequest;
import com.gft.clinica_service.exceptions.BusinessException;
import com.gft.clinica_service.exceptions.ResourceNotFoundException;
import com.gft.clinica_service.dtos.MessageResponse;
import com.gft.clinica_service.dtos.PacienteResponse;
import com.gft.clinica_service.models.Complexidade;
import com.gft.clinica_service.models.Consulta;
import com.gft.clinica_service.models.Medico;
import com.gft.clinica_service.models.Prioridade;
import com.gft.clinica_service.models.Sintoma;
import com.gft.clinica_service.models.Status;
import com.gft.clinica_service.publishers.ConsultaPublisher;
import com.gft.clinica_service.repositories.ConsultaRepository;

@Service
public class ConsultaService {

    private final DoencaService doencaService;

    private final ConsultaRepository consultaRepository;

    private final MedicoService medicoService;
    private final SintomaService sintomaService;

    private final AgendamentoClient agendamentoClient;
    private final ProcedimentoClient procedimentoClient;

    private final ConsultaPublisher consultaPublisher;

    ConsultaService(ConsultaRepository consultaRepository,
            MedicoService medicoService,
            AgendamentoClient agendamentoClient,
            SintomaService sintomaService,
            ProcedimentoClient procedimentoClient,
            ConsultaPublisher consultaPublisher, DoencaService doencaService) {
        this.consultaRepository = consultaRepository;
        this.medicoService = medicoService;
        this.agendamentoClient = agendamentoClient;
        this.sintomaService = sintomaService;
        this.procedimentoClient = procedimentoClient;
        this.consultaPublisher = consultaPublisher;
        this.doencaService = doencaService;
    }

    private Complexidade determinarComplexidade(List<Sintoma> sintomas) {
        boolean emergencial = sintomas.stream()
                .anyMatch(s -> s.getPrioridade() == Prioridade.EMERGENCIAL);

        if (emergencial) {
            return Complexidade.ALTA;
        }

        boolean alta = sintomas.stream()
                .anyMatch(s -> s.getPrioridade() == Prioridade.ALTA);

        if (alta) {
            return Complexidade.ALTA;
        }

        return Complexidade.BAIXA;
    }

    private ConsultaDTO toConsultaDTO(Consulta consulta) {
        PacienteResponse paciente = this.getPacienteByCpf(consulta.getCpfPaciente());

        return ConsultaDTO.builder()
                .crmMedico(consulta.getCrmMedico())
                .nomePaciente(paciente.getNome())
                .dataHora(consulta.getDataHora())
                .sintomas(consulta.getSintomas())
                .status(consulta.getStatus())
                .complexidade(consulta.getComplexidade())
                .build();
    }

    public PacienteResponse getPacienteByCpf(String cpf) {
        return agendamentoClient.getPacienteByCpf(cpf);
    }

    public PacienteResponse getPacienteByNome(String nome) {
        return agendamentoClient.getPacienteByNome(nome);
    }

    // UTILIZADO QUANDO CONSULTA FOR AGENDADA
    public Consulta createConsulta(AgendaRequest request) {
        Consulta newConsulta = new Consulta();

        Medico medico = medicoService.findMedicoByEspecialidade(request.getEspecialidadeMed()).getFirst();

        ConsultaDTO consultaExists = this.findConsultasByCrm(medico.getCrm()).stream()
                .filter(c -> c.getDataHora().equals(request.getDataHora()))
                .findFirst()
                .orElse(null);

        if (consultaExists != null) {
            throw new BusinessException("Consulta já agendada para este médico e horário.");
        }

        newConsulta.setCodigoAgendamento(request.getCodigoAgendamento());
        newConsulta.setCpfPaciente(request.getCpfPaciente());
        newConsulta.setCrmMedico(medico.getCrm());
        newConsulta.setDataHora(request.getDataHora());
        newConsulta.setStatus(Status.AGENDADO);

        return consultaRepository.save(newConsulta);

    }

    public Consulta findConsultaById(UUID id) {
        Optional<Consulta> consulta = this.consultaRepository.findById(id);

        return consulta.orElseThrow(() -> new ResourceNotFoundException("Consulta não encontrada. ID: " + id));
    }

    public Consulta findConsultaByDataHora(LocalDateTime dataHora) {
        List<Consulta> consultas = this.consultaRepository.findByDataHora(dataHora);

        if (consultas.isEmpty()) {
            return null;
        }

        return consultas.get(0);
    }

    public List<ConsultaDTO> findConsultas() {
        List<Consulta> consultas = this.consultaRepository.findAll();

        return consultas.stream()
                .map(this::toConsultaDTO)
                .toList();
    }

    public List<ConsultaDTO> findConsultasByCrm(String crm) {
        List<Consulta> consultas = this.consultaRepository.findByCrmMedico(crm);

        return consultas.stream()
                .map(this::toConsultaDTO)
                .toList();
    }

    public List<ConsultaDTO> findConsultasByNamePaciente(String nome) {
        PacienteResponse paciente = this.getPacienteByNome(nome);
        List<Consulta> consultas = this.consultaRepository.findByCpfPaciente(paciente.getCpf());

        return consultas.stream()
                .map(this::toConsultaDTO)
                .toList();
    }

    // UTILIZADO QUANDO CONSULTA FOR ATENDIDA
    public ConsultaResponse updateConsulta(ConsultaRequest newConsulta) {
        Consulta updatedConsulta;

        if (newConsulta.getCodigoConsulta() != null) {
            updatedConsulta = this.findConsultaById(newConsulta.getCodigoConsulta());
        } else if (newConsulta.getHorario() != null) {
            updatedConsulta = this.findConsultaByDataHora(newConsulta.getHorario());
        } else {
            throw new IllegalArgumentException("É necessário informar Código OU Horário da consulta.");
        }

        PacienteResponse paciente = this.getPacienteByCpf(newConsulta.getCpfPaciente());

        List<Sintoma> sintomas = this.sintomaService.findSintomaByName(newConsulta.getSintomas());

        Complexidade complexidade = this.determinarComplexidade(sintomas);

        updatedConsulta.setCpfPaciente(paciente.getCpf());
        updatedConsulta.setSintomas(newConsulta.getSintomas());
        updatedConsulta.setComplexidade(complexidade);

        updatedConsulta.setStatus(Status.FINALIZADO);

        this.consultaPublisher.publishConsultaFinalizada(updatedConsulta.getCodigoAgendamento());

        ConsultaResponse response = new ConsultaResponse();

        response.setPossiveisDoencas(this.doencaService
                .findDoencaBySintomas(newConsulta.getSintomas()));

        if (response.getPossiveisDoencas().isEmpty()) {
            response.setMessage(
                    "Consulta finalizada. Não foi possível encontrar doenças compatíveis com os sintomas inseridos");
        }

        response.setMessage("Consulta finalizada. Possíveis doenças e seus tratamentos: ");

        this.consultaRepository.save(updatedConsulta);

        return response;
    }

    public void cancelarConsulta(UUID id) {
        Consulta consulta = this.consultaRepository.findByCodigoAgendamento(id).orElseThrow(() -> new ResourceNotFoundException("Não foi possível encontrar a consulta. ID agendamento: " + id));

        consulta.setStatus(Status.CANCELADO);

        this.consultaRepository.save(consulta);
    }

    public void deleteConsultaById(UUID id) {
        this.findConsultaById(id);

        this.consultaRepository.deleteById(id);
    }

    public MessageResponse setExameAltaComplexidade(ProcedimentoRequest request) {
        PacienteResponse paciente = this.getPacienteByCpf(request.getCpfPaciente());

        UUID codigoExame = this.procedimentoClient.createProcedimento(request);

        MessageResponse procedimentoResponse = new MessageResponse();
        procedimentoResponse.setCodigo(codigoExame);
        procedimentoResponse.setMessage(
                "Exame registrado sobre o CPF: " + paciente.getCpf() + ", por favor, agendar horário no sistema.");

        return procedimentoResponse;
    }

}
