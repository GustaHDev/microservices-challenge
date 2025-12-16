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
@Table(name = "doencas")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Doenca {

    public interface CreateDoenca {}
    public interface UpdateDoenca {}

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "O campo nome é obrigatório!", groups = {CreateDoenca.class, UpdateDoenca.class})
    @Column(nullable = false)
    private String nome;
    
    @ElementCollection
    @CollectionTable(
        name = "doenca_sintoma",
        joinColumns = @JoinColumn(name = "doenca_id")
    )
    @Column
    private List<String> sintomas;
    
    @NotNull(message = "O campo tratamentos é obrigatório!", groups = {CreateDoenca.class, UpdateDoenca.class})
    @Column(nullable = false)
    private List<String> tratamentos;

}
