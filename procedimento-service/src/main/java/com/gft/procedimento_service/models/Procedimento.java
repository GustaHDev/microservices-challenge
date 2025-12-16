package com.gft.procedimento_service.models;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "procedimentos")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Procedimento {

    public interface createProcedimento {
    }

    public interface updateProcedimento {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "O CPF do paciente é obrigatório.", groups = { createProcedimento.class,
            updateProcedimento.class })
    @Column(nullable = false)
    private String pacienteCpf;

    @NotBlank(message = "O tipo do procedimento é obrigatório.", groups = { createProcedimento.class,
            updateProcedimento.class })
    @Column(nullable = false)
    private String tipoProcedimento;

    private Complexidade complexidade;

    @NotBlank(message = "O horário é obrigatório.", groups = { createProcedimento.class,
            updateProcedimento.class })
    @Column(nullable = false)
    private LocalDateTime dataHora;

    private Status status;

}
