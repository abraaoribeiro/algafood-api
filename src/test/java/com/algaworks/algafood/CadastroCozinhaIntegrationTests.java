package com.algaworks.algafood;

import com.algaworks.algafood.domain.exception.CozinhaNaoEncotradaException;
import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.service.CadastroCozinhaService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolationException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class CadastroCozinhaIntegrationTests {

    @Autowired
    private CadastroCozinhaService cadastroCozinhaService;

    @Test
    void deveCadastrar_QuandoCozinhaComSucesso() {
        Cozinha novaCozinha = new Cozinha();
        novaCozinha.setNome("Nova Cozinha");

        cadastroCozinhaService.salvar(novaCozinha);

        assertThat(novaCozinha).isNotNull();

    }

    @Test
    void deveCadastrar_QuandoCozinhaSemNome() {
        Cozinha novaCozinha = new Cozinha();
        novaCozinha.setNome(null);

        Assertions.assertThrows(ConstraintViolationException.class, () -> cadastroCozinhaService.salvar(novaCozinha));

        assertThat(novaCozinha).isNotNull();
    }

    @Test
    void deveFalhar_QuandoExcluirCozinhaEmUso(){
        Long idCozinha = 1L;
        Assertions.assertThrows(EntidadeEmUsoException.class, () -> cadastroCozinhaService.excluirPorId(idCozinha));
    }

    @Test
    void deveFalhar_QuandoExcluirCozinhaInexistente(){
        Long idCozinha = 100L;
        Assertions.assertThrows(CozinhaNaoEncotradaException.class, () -> cadastroCozinhaService.excluirPorId(idCozinha));
    }

}
