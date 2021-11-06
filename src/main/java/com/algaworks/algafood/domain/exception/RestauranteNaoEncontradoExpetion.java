package com.algaworks.algafood.domain.exception;

public class RestauranteNaoEncontradoExpetion extends  EntidadeNaoEncontradaException{

    private static final long serialVersionUID = 1L;

    public RestauranteNaoEncontradoExpetion(String mensagem) {
        super(mensagem);
    }

    public RestauranteNaoEncontradoExpetion(Long id){
        this(String.format("Não existe cadastro de restaurante com codigo %d",id));
    }
}
