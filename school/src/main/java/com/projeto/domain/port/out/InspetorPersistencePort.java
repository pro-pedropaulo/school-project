package com.projeto.domain.port.out;

import com.projeto.domain.model.Inspetor;

import java.util.Optional;

public interface InspetorPersistencePort {
    Inspetor salvar(Inspetor inspetor);
    Optional<Inspetor> buscarPorId(Long id);
}