package com.projeto.domain.service;

import com.projeto.domain.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.*;

class TokenServiceTest {

    private TokenService tokenService;

    @BeforeEach
    void setUp() {
        tokenService = new TokenService();
        ReflectionTestUtils.setField(tokenService, "secret", "minha-chave-secreta-para-testes-1234567890123456");
        ReflectionTestUtils.setField(tokenService, "expirationHours", 2L);
    }

    @Nested
    @DisplayName("gerarToken")
    class GerarToken {

        @Test
        void retornaTokenValidoParaUsuarioValido() {
            Usuario usuario = new Usuario();
            usuario.setLogin("usuarioTeste");

            String token = tokenService.gerarToken(usuario);

            assertThat(token).isNotBlank();
        }
    }

    @Nested
    @DisplayName("validarToken")
    class ValidarToken {

        @Test
        void retornaLoginQuandoTokenValido() {
            Usuario usuario = new Usuario();
            usuario.setLogin("usuarioTeste");

            String token = tokenService.gerarToken(usuario);

            String subject = tokenService.validarToken(token);

            assertThat(subject).isEqualTo("usuarioTeste");
        }

        @Test
        void retornaStringVaziaQuandoTokenInvalido() {
            String subject = tokenService.validarToken("token.invalido");

            assertThat(subject).isEmpty();
        }
    }
}