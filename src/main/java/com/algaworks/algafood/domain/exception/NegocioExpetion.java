package com.algaworks.algafood.domain.exception;

public class NegocioExpetion  extends  RuntimeException{

    private static final long serialVersionUID = 1L;

    public NegocioExpetion(String mensagem){
        super(mensagem);
    }

    public NegocioExpetion(String mensagem, Throwable causa){
        super(mensagem,causa);
    }
}
