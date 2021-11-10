package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.domain.exception.EstadoNaoEncotradaException;
import com.algaworks.algafood.domain.exception.NegocioExpetion;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.repository.CidadeRepository;
import com.algaworks.algafood.domain.service.CadastroCidadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/cidades")
@RequiredArgsConstructor
public class CidadeController {

    private final CidadeRepository cidadeRepository;

    private final CadastroCidadeService cadastroCidadeService;

    @GetMapping
    public List<Cidade> buscarTodos() {
        return cidadeRepository.findAll();
    }

    @GetMapping("/{id}")
    public Cidade buscarPorId(@PathVariable Long id) {
        return cadastroCidadeService.buscarOuFalhar(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cidade adicionar(@Valid @RequestBody Cidade cidade) {
        try {
            return cadastroCidadeService.salvar(cidade);
        }catch (EstadoNaoEncotradaException e){
            throw new NegocioExpetion(e.getMessage(),e);
        }
    }

    @PutMapping("/{id}")
    public Cidade atualizar(@Valid @PathVariable Long id, @RequestBody Cidade cidade) {
        try {
            Cidade cidadeAtual = cadastroCidadeService.buscarOuFalhar(id);

            BeanUtils.copyProperties(cidade, cidadeAtual, "id");
            return cadastroCidadeService.salvar(cidadeAtual);
        } catch (EstadoNaoEncotradaException e) {
            throw new NegocioExpetion(e.getMessage(),e);
        }

    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id) {
        cadastroCidadeService.excluirPorId(id);
    }
}
