package com.projeto.school.domain.port.out;

import com.projeto.domain.model.Professor;

import java.util.Optional;

public interface ProfessorPersistencePort {
    Professor salvar(Professor professor);
    Optional<Professor> buscarPorId(Long id);
}