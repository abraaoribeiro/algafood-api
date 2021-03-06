package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.domain.exception.CozinhaNaoEncotradaException;
import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CadastroCozinhaService {

    public static final String MSG_COZINHA_EM_USO = "Cozinha de codigo %d não pode ser removida, pois está em uso";

    private final CozinhaRepository cozinhaRepository;

    public Cozinha salvar(Cozinha cozinha) {
        return cozinhaRepository.save(cozinha);
    }

    public void excluirPorId(Long id) {
        try {
            cozinhaRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new CozinhaNaoEncotradaException(id);
        } catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException(String.format(MSG_COZINHA_EM_USO, id));
        }
    }

    public Cozinha buscarOuFalhar(Long id){
        return cozinhaRepository.findById(id)
                .orElseThrow(() -> new CozinhaNaoEncotradaException(id));
    }
}
