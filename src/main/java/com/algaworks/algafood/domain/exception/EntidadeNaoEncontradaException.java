package com.algaworks.algafood.domain.exception;

public abstract class EntidadeNaoEncontradaException extends NegocioExpetion {

    private static final long serialVersionUID = 1L;

    protected EntidadeNaoEncontradaException(String mensagem) {
        super(mensagem);
    }
}
