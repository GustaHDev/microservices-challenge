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

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "procedimento_agendado")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProcedimentoAgendado {

    public interface CreateProcedimento {}

    public interface UpdateProcedimento {}

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @NotNull(message = "O campo paciente é obrigatório!", groups = { CreateProcedimento.class, UpdateProcedimento.class })
    @JoinColumn(name = "paciente_id", referencedColumnName = "id", nullable = false)
    private Paciente paciente;

    @NotNull(message = "O campo horario é obrigatório!", groups = { CreateProcedimento.class, UpdateProcedimento.class })
    @Column(nullable = false)
    private LocalDateTime dataHora;

    @NotBlank(message = "O campo tipoExame é obrigatório!", groups = { CreateProcedimento.class, UpdateProcedimento.class })
    @Column(nullable = false)
    private String tipoExame;

    @Column(name = "status_procedimento")
    private Status status;

    @Column(name = "complexidade_procedimento")
    private Complexidade complexidade;

}
