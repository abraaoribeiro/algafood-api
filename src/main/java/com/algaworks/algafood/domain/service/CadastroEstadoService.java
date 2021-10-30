package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EstadoNaoEncotradaException;
import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.repository.EstadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CadastroEstadoService {

    private final EstadoRepository estadoRepository;

    public Estado salvar(Estado estado) {
        return estadoRepository.save(estado);
    }

    public void excluirPorId(Long id) {
        try {
            estadoRepository.deleteById(id);
        }catch (EmptyResultDataAccessException e){
            throw new EstadoNaoEncotradaException(String.format("Não existe um cadastro de estado com o codigo %d", id));
        }catch (DataIntegrityViolationException e){
            throw new EntidadeEmUsoException(String.format("Estado de código %d não pode ser removido, pois está em uso", id));
        }
    }
}