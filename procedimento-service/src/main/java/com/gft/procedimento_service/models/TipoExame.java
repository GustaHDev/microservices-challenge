package com.gft.procedimento_service.models;

import java.util.Arrays;

import com.gft.procedimento_service.exceptions.ResourceNotFoundException;

public enum TipoExame {

    SANGUE(Complexidade.BAIXA),
    URINA(Complexidade.BAIXA),
    RAIO_X(Complexidade.BAIXA),
    
    TOMOGRAFIA(Complexidade.ALTA),
    RESSONANCIA_MAGNETICA(Complexidade.ALTA),
    RESSONANCIA(Complexidade.ALTA),
    ULTRASSONOGRAFIA(Complexidade.ALTA),;

    private final Complexidade complexidade;

    TipoExame(Complexidade complexidade) {
        this.complexidade = complexidade;
    }

    public Complexidade getComplexidade() {
        return complexidade;
    }

    public static TipoExame fromNome(String nome) {
        return Arrays.stream(values())
                     .filter(e -> e.name().equalsIgnoreCase(nome))
                     .findFirst()
                     .orElseThrow(() -> new ResourceNotFoundException("Exame não encontrado. Nome: " + nome));
    }
}
