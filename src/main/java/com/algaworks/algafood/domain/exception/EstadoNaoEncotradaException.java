package com.algaworks.algafood.domain.exception;

public class EstadoNaoEncotradaException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public EstadoNaoEncotradaException(String mensagem) {
        super(mensagem);
    }
}
