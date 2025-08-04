package com.projeto.infrastructure.repository.jpa;

import com.projeto.domain.model.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProfessorRepository extends JpaRepository<Professor, Long> {

    @Query("SELECT p FROM Professor p WHERE p.id = :id")
    Optional<Professor> buscarPorId(@Param("id") Long id);
}