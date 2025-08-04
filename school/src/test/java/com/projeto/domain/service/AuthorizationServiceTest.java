package com.projeto.domain.service;

import com.projeto.domain.model.Usuario;
import com.projeto.infrastructure.repository.jpa.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthorizationServiceTest {

    private final UsuarioRepository usuarioRepository = mock(UsuarioRepository.class);
    private final AuthorizationService authorizationService = new AuthorizationService(usuarioRepository);

    @Nested
    @DisplayName("loadUserByUsername")
    class LoadUserByUsername {

        @Test
        void retornaUsuarioQuandoLoginExiste() {
            Usuario usuario = new Usuario();
            usuario.setLogin("usuarioTeste");

            when(usuarioRepository.buscarPorLogin("usuarioTeste")).thenReturn(Optional.of(usuario));

            var result = authorizationService.loadUserByUsername("usuarioTeste");

            assertThat(result).isEqualTo(usuario);
        }

        @Test
        void lancaExcecaoQuandoUsuarioNaoExiste() {
            when(usuarioRepository.buscarPorLogin("naoExiste")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> authorizationService.loadUserByUsername("naoExiste"))
                    .isInstanceOf(UsernameNotFoundException.class)
                    .hasMessageContaining("Usuário não encontrado com o login: naoExiste");
        }
    }
}