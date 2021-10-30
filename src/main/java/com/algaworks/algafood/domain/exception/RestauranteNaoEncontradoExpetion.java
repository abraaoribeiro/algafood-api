package com.algaworks.algafood.domain.exception;

public class RestauranteNaoEncontradoExpetion extends  RuntimeException{

    private static final long serialVersionUID = 1L;

    public RestauranteNaoEncontradoExpetion(String mensagem) {
        super(mensagem);
    }
}
