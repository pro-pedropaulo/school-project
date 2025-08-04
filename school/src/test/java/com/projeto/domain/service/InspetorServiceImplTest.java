package com.projeto.domain.service;

import com.projeto.application.exception.BusinessException;
import com.projeto.application.exception.ResourceNotFoundException;
import com.projeto.domain.model.Professor;
import com.projeto.domain.model.Role;
import com.projeto.domain.model.Usuario;
import com.projeto.domain.port.out.UsuarioPersistencePort;
import com.projeto.school.domain.port.out.ProfessorPersistencePort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InspetorServiceImplTest {

    @Mock
    private ProfessorPersistencePort professorPersistencePort;
    @Mock
    private UsuarioPersistencePort usuarioPersistencePort;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private InspetorServiceImpl service;

    @Nested
    @DisplayName("cadastrarProfessor")
    class CadastrarProfessor {

        @Test
        void deveCadastrarProfessorComDadosValidos() {
            String nome = "João";
            String login = "joao";
            String senha = "senha";
            String senhaCriptografada = "senhaCripto";

            when(usuarioPersistencePort.buscarPorLogin(login)).thenReturn(Optional.empty());
            when(passwordEncoder.encode(senha)).thenReturn(senhaCriptografada);
            when(professorPersistencePort.salvar(any(Professor.class)))
                    .thenAnswer(invocation -> {
                        Professor p = invocation.getArgument(0);
                        p.setId(10L);
                        return p;
                    });

            Professor professor = service.cadastrarProfessor(nome, login, senha);

            assertThat(professor.getNome()).isEqualTo(nome);
            assertThat(professor.getUsuario().getLogin()).isEqualTo(login);
            assertThat(professor.getUsuario().getSenha()).isEqualTo(senhaCriptografada);
            assertThat(professor.getUsuario().getRole()).isEqualTo(Role.PROFESSOR);
            assertThat(professor.getUsuario().isAtivo()).isTrue();
            assertThat(professor.getId()).isEqualTo(10L);

            verify(usuarioPersistencePort).buscarPorLogin(login);
            verify(passwordEncoder).encode(senha);
            verify(professorPersistencePort).salvar(any(Professor.class));
        }

        @Test
        void deveLancarExcecaoQuandoLoginJaExiste() {
            String login = "joao";
            when(usuarioPersistencePort.buscarPorLogin(login)).thenReturn(Optional.of(new Usuario()));

            assertThatThrownBy(() -> service.cadastrarProfessor("nome", login, "senha"))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("Login já está em uso.");

            verify(usuarioPersistencePort).buscarPorLogin(login);
            verifyNoMoreInteractions(professorPersistencePort);
        }
    }

    @Nested
    @DisplayName("desativarProfessor")
    class DesativarProfessor {

        @Test
        void deveDesativarProfessorComIdValido() {
            Long id = 5L;
            Usuario usuario = new Usuario();
            usuario.setAtivo(true);
            Professor professor = new Professor();
            professor.setUsuario(usuario);

            when(professorPersistencePort.buscarPorId(id)).thenReturn(Optional.of(professor));
            when(usuarioPersistencePort.salvar(usuario)).thenReturn(usuario);

            service.desativarProfessor(id);

            assertThat(usuario.isAtivo()).isFalse();
            verify(professorPersistencePort).buscarPorId(id);
            verify(usuarioPersistencePort).salvar(usuario);
        }

        @Test
        void deveLancarExcecaoQuandoProfessorNaoExiste() {
            Long id = 99L;
            when(professorPersistencePort.buscarPorId(id)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.desativarProfessor(id))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Professor")
                    .hasMessageContaining("id")
                    .hasMessageContaining(id.toString());

            verify(professorPersistencePort).buscarPorId(id);
            verifyNoMoreInteractions(usuarioPersistencePort);
        }
    }
}