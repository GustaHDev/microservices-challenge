package com.gft.agendamento_service.models;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
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

    @Id
    @GeneratedValue
    private UUID id;

    @NotBlank(message = "O campo nome é obrigatório!")
    @Column(nullable = false)
    private String nome;

    @NotBlank(message = "O campo cpf é obrigatório!")
    @Column(nullable = false)
    private String cpf;

    @NotBlank(message = "O campo idade é obrigatório!")
    @Column(nullable = false)
    private Integer idade;

    @NotBlank(message = "O campo sexo é obrigatório!")
    @Column(nullable = false)
    private String sexo;
}
