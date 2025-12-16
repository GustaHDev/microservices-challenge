package com.gft.clinica_service.models;

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
@Table(name = "sintomas")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Sintoma {

    public interface CreateSintoma {}
    public interface UpdateSintoma {}

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "O campo nome é obrigatório!", groups = {CreateSintoma.class, UpdateSintoma.class})
    @Column(nullable = false)
    private String nome;

    @NotNull(message = "O campo prioridade é obrigatório!", groups = {CreateSintoma.class, UpdateSintoma.class})
    @Column(nullable = false)
    private Prioridade prioridade;

    @ElementCollection
    @CollectionTable(
        name = "sintoma_doenca",
        joinColumns = @JoinColumn(name = "sintoma_id")
    )
    @Column
    private List<String> doencas;

}
