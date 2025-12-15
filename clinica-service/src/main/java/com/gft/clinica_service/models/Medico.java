package com.gft.clinica_service.models;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
@Table(name = "medicos", uniqueConstraints = @UniqueConstraint(columnNames = "crm"))
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Medico {

    public interface CreateMedico {}
    public interface UpdateMedico {}

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "O campo nome é obrigatório!", groups = {CreateMedico.class, UpdateMedico.class})
    @Column(nullable = false)
    private String nome;
    
    @NotBlank(message = "O campo crm é obrigatório!", groups = {CreateMedico.class, UpdateMedico.class})
    @Column(nullable = false, unique = true)
    private String crm;
    
    @NotBlank(message = "O campo especialidade é obrigatório!", groups = {CreateMedico.class, UpdateMedico.class})
    @Column(nullable = false)
    private String especialidade;

}
