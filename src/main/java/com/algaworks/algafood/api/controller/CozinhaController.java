package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.domain.exception.CozinhaNaoEncotradaException;
import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.domain.service.CadastroCozinhaService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cozinhas")
@RequiredArgsConstructor
public class CozinhaController {

    private final CozinhaRepository cozinhaRepository;

    private final CadastroCozinhaService cadastroCozinhaService;

    @GetMapping
    private List<Cozinha> buscarTodos() {
        return cozinhaRepository.findAll();
    }

    @GetMapping({"/id"})
    private ResponseEntity<Cozinha> buscarPorId(@PathVariable  Long id){
        return cozinhaRepository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    private ResponseEntity<Cozinha> adicionar(@RequestBody Cozinha cozinha){
       return ResponseEntity.status(HttpStatus.CREATED).body(cadastroCozinhaService.salvar(cozinha));
    }

    @PutMapping("/{id}")
    private ResponseEntity<Cozinha> atualizar(@PathVariable Long id, @RequestBody Cozinha cozinha){
        Optional<Cozinha> cozinhaAtual = cozinhaRepository.findById(id);

        if (cozinhaAtual.isPresent()) {
            BeanUtils.copyProperties(cozinha, cozinhaAtual.get(), "id");

            Cozinha cozinhaSalva = cadastroCozinhaService.salvar(cozinhaAtual.get());
            return ResponseEntity.ok(cozinhaSalva);
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<?> remover(@PathVariable Long id){
        try {
            cadastroCozinhaService.excluirPorId(id);
            return ResponseEntity.noContent().build();

        } catch (CozinhaNaoEncotradaException e) {
            return ResponseEntity.notFound().build();

        } catch (EntidadeEmUsoException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(e.getMessage());
        }
    }


}
