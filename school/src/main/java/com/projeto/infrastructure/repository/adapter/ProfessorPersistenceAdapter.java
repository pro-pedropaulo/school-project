package com.projeto.infrastructure.repository.adapter;

import com.projeto.domain.model.Professor;
import com.projeto.infrastructure.repository.jpa.ProfessorRepository;
import com.projeto.school.domain.port.out.ProfessorPersistencePort;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ProfessorPersistenceAdapter implements ProfessorPersistencePort {

    private final ProfessorRepository professorRepository;

    public ProfessorPersistenceAdapter(ProfessorRepository professorRepository) {
        this.professorRepository = professorRepository;
    }

    @Override
    public Professor salvar(Professor professor) {
        return professorRepository.save(professor);
    }

    @Override
    public Optional<Professor> buscarPorId(Long id) {
        return professorRepository.buscarPorId(id);
    }
}