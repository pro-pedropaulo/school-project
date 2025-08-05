package com.projeto.application.rest.controller;

import com.projeto.application.dto.CriarProfessorDTO;
import com.projeto.application.dto.PresencaResponseDTO;
import com.projeto.application.dto.ProfessorResponseDTO;
import com.projeto.application.dto.RegistrarPresencaDTO;
import com.projeto.application.mapper.PresencaMapper;
import com.projeto.application.mapper.ProfessorMapper;
import com.projeto.domain.model.Presenca;
import com.projeto.domain.model.Professor;
import com.projeto.domain.model.Role;
import com.projeto.domain.model.Usuario;
import com.projeto.domain.port.in.InspetorUseCase;
import com.projeto.domain.port.in.PresencaUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InspetorControllerTest {

    @Mock
    private InspetorUseCase inspetorUseCase;
    @Mock
    private ProfessorMapper professorMapper;
    @Mock
    private PresencaUseCase presencaUseCase;
    @Mock
    private PresencaMapper presencaMapper;

    @InjectMocks
    private InspetorController controller;

    @Nested
    @DisplayName("cadastrarProfessor")
    class CadastrarProfessor {
        @Test
        void retornaResponseCreatedQuandoCadastroValido() {
            CriarProfessorDTO dto = new CriarProfessorDTO("nome", "login", "senha");
            Professor professor = new Professor();
            ProfessorResponseDTO responseDTO = new ProfessorResponseDTO(1L, "nome", "login");

            when(inspetorUseCase.cadastrarProfessor(dto.nome(), dto.login(), dto.senha())).thenReturn(professor);
            when(professorMapper.toResponseDTO(professor)).thenReturn(responseDTO);

            ResponseEntity<ProfessorResponseDTO> response = controller.cadastrarProfessor(dto);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            assertThat(response.getBody()).isEqualTo(responseDTO);
        }
    }

    @Nested
    @DisplayName("desativarProfessor")
    class DesativarProfessor {
        @Test
        void retornaNoContentQuandoDesativacaoValida() {
            Long id = 1L;
            ResponseEntity<Void> response = controller.desativarProfessor(id);
            verify(inspetorUseCase).desativarProfessor(id);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
            assertThat(response.getBody()).isNull();
        }
    }

    @Nested
    @DisplayName("registrarPresencaPropria")
    class RegistrarPresencaPropria {
        @Test
        void retornaCreatedComPresencaRegistrada() {
            LocalDate data = LocalDate.now();
            RegistrarPresencaDTO dto = new RegistrarPresencaDTO(data);
            Usuario usuario = new Usuario(1L, "inspetor.logado", "senha", Role.INSPETOR, true);
            Presenca presenca = new Presenca();
            PresencaResponseDTO presencaDTO = new PresencaResponseDTO(1L, data, "inspetor.logado");

            when(presencaUseCase.registrarPresenca(usuario.getLogin(), data)).thenReturn(presenca);
            when(presencaMapper.toResponseDTO(presenca)).thenReturn(presencaDTO);

            ResponseEntity<PresencaResponseDTO> response = controller.registrarPresencaPropria(dto, usuario);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            assertThat(response.getBody()).isEqualTo(presencaDTO);
        }
    }

    @Nested
    @DisplayName("verPresencasProprias")
    class VerPresencasProprias {
        @Test
        void retornaOkComHistoricoDePresencas() {
            Usuario usuario = new Usuario(1L, "inspetor.logado", "senha", Role.INSPETOR, true);
            Presenca presenca = new Presenca();
            PresencaResponseDTO presencaDTO = new PresencaResponseDTO(1L, LocalDate.now(), "inspetor.logado");

            when(presencaUseCase.buscarPresencasPorUsuario(usuario.getLogin())).thenReturn(List.of(presenca));
            when(presencaMapper.toResponseDTO(presenca)).thenReturn(presencaDTO);

            ResponseEntity<List<PresencaResponseDTO>> response = controller.verPresencasProprias(usuario);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().size()).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("verPresencasProfessores")
    class VerPresencasProfessores {
        @Test
        void retornaOkComListaDePresencas() {
            LocalDate data = LocalDate.now();
            when(presencaUseCase.buscarPresencasPorDataERole(data, Role.PROFESSOR)).thenReturn(Collections.emptyList());

            ResponseEntity<List<PresencaResponseDTO>> response = controller.verPresencasProfessores(data);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody()).isEmpty();
        }
    }
}