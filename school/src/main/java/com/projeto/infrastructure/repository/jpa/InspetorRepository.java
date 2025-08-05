package com.projeto.infrastructure.repository.jpa;

import com.projeto.domain.model.Inspetor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface InspetorRepository extends JpaRepository<Inspetor, Long> {

    @Query("SELECT i FROM Inspetor i WHERE i.id = :id")
    Optional<Inspetor> buscarPorId(@Param("id") Long id);

    @Query("SELECT i FROM Inspetor i WHERE i.usuario.ativo = true")
    List<Inspetor> findAllAtivos();
}