package com.algaworks.algafood.api.exceptionhandler.enums;

import lombok.Getter;

@Getter
public enum ProblemType {

    ERRO_SISTEMA("/erro-de-sistema", "Erro de Sistema"),
    PARAMETRO_INVALIDO("/parametro-invalido", "Parâmetro inválido"),
    MENSAGEM_INCOMPREENSIVEL("/mensagem-incompreensivel", "Mensagem Incompreensivel"),
    RECURSO_NAO_ENCONTRADA("/recurso-nao-encontrada", "Recurso não encontrada"),
    ENTIDADE_EM_USO("/entidade-em-uso", "Entidade em uso"),
    ERRO_NEGOCIO("/erro-negocio", "Violação de regra de negócio");

    private String uri;
    private String title;

    ProblemType(String path, String title){
        this.uri = "" + path;
        this.title = title;
    }

}
