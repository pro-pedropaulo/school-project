package com.projeto.application.rest.controller;

import com.projeto.application.dto.AuthenticationDTO;
import com.projeto.application.dto.TokenDTO;
import com.projeto.domain.model.Usuario;
import com.projeto.domain.service.TokenService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private TokenService tokenService;
    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthenticationController authenticationController;

    @Nested
    @DisplayName("login")
    class Login {

        @Test
        void retornaTokenQuandoCredenciaisValidas() {
            AuthenticationDTO dto = new AuthenticationDTO("usuario", "senha");
            Usuario usuario = new Usuario();
            String token = "jwt-token";

            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(usuario);
            when(tokenService.gerarToken(usuario)).thenReturn(token);

            ResponseEntity<TokenDTO> response = authenticationController.login(dto);

            assertThat(response.getStatusCodeValue()).isEqualTo(200);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().token()).isEqualTo(token);
        }

        @Test
        void lancaExcecaoQuandoCredenciaisInvalidas() {
            AuthenticationDTO dto = new AuthenticationDTO("usuario", "senha");

            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenThrow(new BadCredentialsException("Credenciais inválidas"));

            assertThatThrownBy(() -> authenticationController.login(dto))
                    .isInstanceOf(BadCredentialsException.class)
                    .hasMessageContaining("Credenciais inválidas");
        }

        @Test
        void lancaExcecaoQuandoTokenServiceFalha() {
            AuthenticationDTO dto = new AuthenticationDTO("usuario", "senha");
            Usuario usuario = new Usuario();

            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(usuario);
            when(tokenService.gerarToken(usuario)).thenThrow(new RuntimeException("Erro ao gerar token"));

            assertThatThrownBy(() -> authenticationController.login(dto))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Erro ao gerar token");
        }
    }
}