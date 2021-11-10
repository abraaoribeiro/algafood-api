package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.repository.EstadoRepository;
import com.algaworks.algafood.domain.service.CadastroEstadoService;
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
@RequestMapping("/estados")
@RequiredArgsConstructor
public class EstadoController {

    private final EstadoRepository estadoRepository;

    private final CadastroEstadoService cadastroEstadoService;

    @GetMapping
    public List<Estado> buscarTodos() {
        return estadoRepository.findAll();
    }

    @GetMapping("/{id}")
    public Estado buscarPorId(@PathVariable Long id) {
        return cadastroEstadoService.buscarOuFalhar(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Estado adicionar(@Valid @RequestBody Estado estado) {
        return cadastroEstadoService.salvar(estado);
    }

    @PutMapping("/{id}")
    public Estado atualizar(@Valid @PathVariable Long id, @RequestBody Estado estado) {
        Estado estadoAtual = cadastroEstadoService.buscarOuFalhar(id);

        BeanUtils.copyProperties(estado, estadoAtual, "id");

        return cadastroEstadoService.salvar(estadoAtual);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id) {
         cadastroEstadoService.excluirPorId(id);
    }

}
