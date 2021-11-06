package com.algaworks.algafood.domain.exception;

public class CidadeNaoEncotradaException extends EntidadeNaoEncontradaException{

    private static final long serialVersionUID = 1L;

    public CidadeNaoEncotradaException(String mensagem){
        super(mensagem);
    }

    public CidadeNaoEncotradaException(Long id){
        this(String.format("Não existe um casdastro de cozinha com o código %d",id));
    }
}
