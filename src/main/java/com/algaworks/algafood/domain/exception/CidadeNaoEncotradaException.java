package com.algaworks.algafood.domain.exception;

public class CidadeNaoEncotradaException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public CidadeNaoEncotradaException(String mensagem){
        super(mensagem);
    }
}
