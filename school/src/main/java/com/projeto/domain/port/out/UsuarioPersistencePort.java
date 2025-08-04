package com.projeto.domain.port.out;

import com.projeto.domain.model.Usuario;

import java.util.Optional;

public interface UsuarioPersistencePort {
    Optional<Usuario> buscarPorLogin(String login);
    Usuario salvar(Usuario usuario);
}