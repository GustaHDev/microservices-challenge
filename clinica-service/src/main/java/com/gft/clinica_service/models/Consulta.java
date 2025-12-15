package com.gft.clinica_service.models;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "consultas")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Consulta {

    public interface CreateConsulta {
    }

    public interface UpdateConsulta {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull(message = "O cpf do paciente é obrigatório!", groups = { CreateConsulta.class, UpdateConsulta.class })
    @Column(nullable = false)
    private String cpfPaciente;

    @NotBlank(message = "O crm do médico é obrigatório!", groups = { CreateConsulta.class, UpdateConsulta.class })
    @Column(nullable = false)
    private String crmMedico;

    @NotNull(message = "O horario é obrigatório!", groups = { CreateConsulta.class, UpdateConsulta.class })
    @Column(nullable = false)
    private LocalDateTime dataHora;

    @ElementCollection
    @CollectionTable(name = "consulta_sintomas", 
    joinColumns = @JoinColumn(name = "consulta_id"))
    @Column
    private List<String> sintomas;

    @Column
    private Status status;

    @Column
    private Complexidade complexidade;

}
