package com.gft.agendamento_service.models;

import java.util.UUID;

import org.hibernate.validator.constraints.br.CPF;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "pacientes", uniqueConstraints = @UniqueConstraint(columnNames = "cpf"))
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Paciente {

    public interface CreatePaciente {}
    public interface UpdatePaciente {}

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "O campo nome é obrigatório!", groups = {CreatePaciente.class, UpdatePaciente.class})
    @Column(nullable = false)
    private String nome;

    @NotBlank(message = "O campo cpf é obrigatório!", groups = {CreatePaciente.class, UpdatePaciente.class})
    @CPF(message = "CPF inválido!", groups = { CreatePaciente.class, UpdatePaciente.class })
    @Column(nullable = false, unique = true)
    private String cpf;

    @NotNull(message = "O campo idade é obrigatório!", groups = {CreatePaciente.class, UpdatePaciente.class})
    @Column(nullable = false)
    private Integer idade;

    @NotBlank(message = "O campo sexo é obrigatório!", groups = {CreatePaciente.class, UpdatePaciente.class})
    @Column(nullable = false)
    private String sexo;
}
