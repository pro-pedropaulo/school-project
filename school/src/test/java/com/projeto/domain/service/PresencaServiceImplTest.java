package com.projeto.domain.service;

import com.projeto.application.exception.BusinessException;
import com.projeto.application.exception.ResourceNotFoundException;
import com.projeto.domain.model.Presenca;
import com.projeto.domain.model.Role;
import com.projeto.domain.model.Usuario;
import com.projeto.domain.port.out.PresencaPersistencePort;
import com.projeto.domain.port.out.UsuarioPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PresencaServiceImplTest {

    @Mock
    private PresencaPersistencePort presencaPersistencePort;
    @Mock
    private UsuarioPersistencePort usuarioPersistencePort;

    @InjectMocks
    private PresencaServiceImpl presencaService;

    private Usuario usuario;
    private LocalDate data;

    @BeforeEach
    void setup() {
        usuario = new Usuario();
        usuario.setLogin("usuario1");
        data = LocalDate.now();
    }

    @Nested
    @DisplayName("registrarPresenca")
    class RegistrarPresenca {

        @Test
        void deveRegistrarPresencaComDadosValidos() {
            when(usuarioPersistencePort.buscarPorLogin("usuario1")).thenReturn(Optional.of(usuario));
            when(presencaPersistencePort.existePresencaPorUsuarioEData(usuario, data)).thenReturn(false);
            when(presencaPersistencePort.salvar(any(Presenca.class))).thenAnswer(invocation -> {
                Presenca p = invocation.getArgument(0);
                p.setId(10L);
                return p;
            });

            Presenca presenca = presencaService.registrarPresenca("usuario1", data);

            assertThat(presenca.getId()).isEqualTo(10L);
            assertThat(presenca.getUsuario()).isEqualTo(usuario);
            assertThat(presenca.getData()).isEqualTo(data);
        }

        @Test
        void deveLancarExcecaoQuandoUsuarioNaoExiste() {
            when(usuarioPersistencePort.buscarPorLogin("usuario1")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> presencaService.registrarPresenca("usuario1", data))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Usuário");
        }

        @Test
        void deveLancarExcecaoQuandoPresencaJaRegistrada() {
            when(usuarioPersistencePort.buscarPorLogin("usuario1")).thenReturn(Optional.of(usuario));
            when(presencaPersistencePort.existePresencaPorUsuarioEData(usuario, data)).thenReturn(true);

            assertThatThrownBy(() -> presencaService.registrarPresenca("usuario1", data))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("Presença já registrada");
        }
    }

    @Nested
    @DisplayName("buscarPresencasPorUsuario")
    class BuscarPresencasPorUsuario {

        @Test
        void deveRetornarListaDePresencas() {
            when(usuarioPersistencePort.buscarPorLogin("usuario1")).thenReturn(Optional.of(usuario));
            Presenca presenca = new Presenca();
            presenca.setId(1L);
            when(presencaPersistencePort.buscarPorUsuario(usuario)).thenReturn(List.of(presenca));

            List<Presenca> resultado = presencaService.buscarPresencasPorUsuario("usuario1");

            assertThat(resultado).hasSize(1);
            assertThat(resultado.get(0).getId()).isEqualTo(1L);
        }

        @Test
        void deveLancarExcecaoQuandoUsuarioNaoExiste() {
            when(usuarioPersistencePort.buscarPorLogin("usuario1")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> presencaService.buscarPresencasPorUsuario("usuario1"))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Usuário");
        }
    }

    @Nested
    @DisplayName("buscarPresencasPorDataERole")
    class BuscarPresencasPorDataERole {

        @Test
        void deveRetornarPresencasPorDataERole() {
            Presenca presenca = new Presenca();
            presenca.setId(2L);
            when(presencaPersistencePort.buscarPorDataERole(data, Role.INSPETOR)).thenReturn(List.of(presenca));

            List<Presenca> resultado = presencaService.buscarPresencasPorDataERole(data, Role.INSPETOR);

            assertThat(resultado).hasSize(1);
            assertThat(resultado.get(0).getId()).isEqualTo(2L);
        }

        @Test
        void deveRetornarListaVaziaQuandoNaoHaPresencas() {
            when(presencaPersistencePort.buscarPorDataERole(data, Role.DIRETOR)).thenReturn(Collections.emptyList());

            List<Presenca> resultado = presencaService.buscarPresencasPorDataERole(data, Role.DIRETOR);

            assertThat(resultado).isEmpty();
        }
    }
}