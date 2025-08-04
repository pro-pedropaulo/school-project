package com.projeto.infrastructure.repository.jpa;

import com.projeto.domain.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @Query("SELECT u FROM Usuario u WHERE u.login = :login")
    Optional<Usuario> buscarPorLogin(@Param("login") String login);
}