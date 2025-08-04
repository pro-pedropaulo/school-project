package com.projeto.domain.service;

import com.projeto.application.exception.BusinessException;
import com.projeto.application.exception.ResourceNotFoundException;
import com.projeto.domain.model.Inspetor;
import com.projeto.domain.model.Role;
import com.projeto.domain.model.Usuario;
import com.projeto.domain.port.out.InspetorPersistencePort;
import com.projeto.domain.port.out.UsuarioPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DiretorServiceImplTest {

    @Mock
    private InspetorPersistencePort inspetorPersistencePort;
    @Mock
    private UsuarioPersistencePort usuarioPersistencePort;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private DiretorServiceImpl diretorService;

    @Nested
    @DisplayName("cadastrarInspetor")
    class CadastrarInspetor {

        @BeforeEach
        void setup() {
            lenient().when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        }

        @Test
        void deveCadastrarInspetorComDadosValidos() {
            when(usuarioPersistencePort.buscarPorLogin("login")).thenReturn(Optional.empty());
            when(inspetorPersistencePort.salvar(any(Inspetor.class))).thenAnswer(invocation -> {
                Inspetor inspetor = invocation.getArgument(0);
                inspetor.setId(1L);
                return inspetor;
            });

            Inspetor inspetor = diretorService.cadastrarInspetor("Nome", "login", "senha");

            assertThat(inspetor.getId()).isEqualTo(1L);
            assertThat(inspetor.getNome()).isEqualTo("Nome");
            assertThat(inspetor.getUsuario().getLogin()).isEqualTo("login");
            assertThat(inspetor.getUsuario().getSenha()).isEqualTo("encodedPassword");
            assertThat(inspetor.getUsuario().getRole()).isEqualTo(Role.INSPETOR);
            assertThat(inspetor.getUsuario().isAtivo()).isTrue();
        }

        @Test
        void deveLancarExcecaoQuandoLoginJaExiste() {
            when(usuarioPersistencePort.buscarPorLogin("login")).thenReturn(Optional.of(new Usuario()));

            assertThatThrownBy(() -> diretorService.cadastrarInspetor("Nome", "login", "senha"))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("Login já está em uso.");
        }
    }

    @Nested
    @DisplayName("desativarInspetor")
    class DesativarInspetor {

        @Test
        void deveDesativarInspetorComIdValido() {
            Usuario usuario = new Usuario();
            usuario.setAtivo(true);
            Inspetor inspetor = new Inspetor();
            inspetor.setId(1L);
            inspetor.setUsuario(usuario);

            when(inspetorPersistencePort.buscarPorId(1L)).thenReturn(Optional.of(inspetor));
            when(usuarioPersistencePort.salvar(any(Usuario.class))).thenReturn(usuario);

            diretorService.desativarInspetor(1L);

            assertThat(usuario.isAtivo()).isFalse();
            verify(usuarioPersistencePort).salvar(usuario);
        }

        @Test
        void deveLancarExcecaoQuandoInspetorNaoExiste() {
            when(inspetorPersistencePort.buscarPorId(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> diretorService.desativarInspetor(99L))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Inspetor");
        }
    }
}