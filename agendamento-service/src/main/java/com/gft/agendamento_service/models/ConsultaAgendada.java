package com.gft.agendamento_service.models;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "consulta_agendada")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ConsultaAgendada {

    public interface CreateConsulta {
    }

    public interface UpdateConsulta {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @NotNull(message = "O campo paciente é obrigatório!", groups = { CreateConsulta.class, UpdateConsulta.class })
    @JoinColumn(name = "paciente_id", referencedColumnName = "id", nullable = false)
    private Paciente paciente;

    @NotNull(message = "O campo horario é obrigatório!", groups = { CreateConsulta.class, UpdateConsulta.class })
    @Future(message = "A consulta deve ser agendada para uma data futura", groups = { CreateConsulta.class, UpdateConsulta.class})
    @Column(nullable = false)
    private LocalDateTime dataHora;

    @NotBlank(message = "O campo especialidade é obrigatório!", groups = { CreateConsulta.class, UpdateConsulta.class })
    @Column(name = "especialidade_med", nullable = false)
    private String especialidadeMed;

    @Column(name = "status_consulta")
    private Status status;

}
