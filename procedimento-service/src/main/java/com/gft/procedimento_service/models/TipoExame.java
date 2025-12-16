package com.gft.procedimento_service.models;

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
}
