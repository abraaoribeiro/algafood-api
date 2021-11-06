package com.algaworks.algafood.domain.exception;

public class EstadoNaoEncotradaException extends EntidadeNaoEncontradaException {

    private static final long serialVersionUID = 1L;

    public EstadoNaoEncotradaException(String mensagem) {
        super(mensagem);
    }

    public EstadoNaoEncotradaException(Long id) {
        this(String.format("Não existe um cadastro de estado com o codigo %d",id));
    }
}
