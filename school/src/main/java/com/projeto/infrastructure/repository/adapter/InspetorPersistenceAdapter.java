package com.projeto.infrastructure.repository.adapter;

import com.projeto.domain.model.Inspetor;
import com.projeto.domain.port.out.InspetorPersistencePort;
import com.projeto.infrastructure.repository.jpa.InspetorRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class InspetorPersistenceAdapter implements InspetorPersistencePort {

    private final InspetorRepository inspetorRepository;

    public InspetorPersistenceAdapter(InspetorRepository inspetorRepository) {
        this.inspetorRepository = inspetorRepository;
    }

    @Override
    public Inspetor salvar(Inspetor inspetor) {
        return inspetorRepository.save(inspetor);
    }

    @Override
    public Optional<Inspetor> buscarPorId(Long id) {
        return inspetorRepository.buscarPorId(id);
    }
}