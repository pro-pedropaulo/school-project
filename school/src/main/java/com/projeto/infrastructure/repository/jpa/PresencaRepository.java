package com.projeto.infrastructure.repository.jpa;

import com.projeto.domain.model.Presenca;
import com.projeto.domain.model.Role;
import com.projeto.domain.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PresencaRepository extends JpaRepository<Presenca, Long> {

    @Query("SELECT COUNT(p) > 0 FROM Presenca p WHERE p.usuario = :usuario AND p.data = :data")
    boolean existsByUsuarioAndData(@Param("usuario") Usuario usuario, @Param("data") LocalDate data);

    @Query("SELECT p FROM Presenca p WHERE p.usuario = :usuario ORDER BY p.data DESC")
    List<Presenca> findByUsuario(@Param("usuario") Usuario usuario);

    @Query("SELECT p FROM Presenca p WHERE p.data = :data AND p.usuario.role = :role")
    List<Presenca> findByDataAndUsuarioRole(@Param("data") LocalDate data, @Param("role") Role role);
}