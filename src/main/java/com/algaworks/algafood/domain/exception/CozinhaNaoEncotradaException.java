package com.algaworks.algafood.domain.exception;

public class CozinhaNaoEncotradaException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CozinhaNaoEncotradaException(String mensagem) {
        super(mensagem);
    }
}
