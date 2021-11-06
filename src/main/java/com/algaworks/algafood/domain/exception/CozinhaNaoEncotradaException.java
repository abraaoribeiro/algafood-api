package com.algaworks.algafood.domain.exception;

public class CozinhaNaoEncotradaException extends EntidadeNaoEncontradaException {

    private static final long serialVersionUID = 1L;

    public CozinhaNaoEncotradaException(String mensagem) {
        super(mensagem);
    }

    public CozinhaNaoEncotradaException(Long id){
        this(String.format("Não existe um cadastro de cozinha com código %d",id));
    }
}
