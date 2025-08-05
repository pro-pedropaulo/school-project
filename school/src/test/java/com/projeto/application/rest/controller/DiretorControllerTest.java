package com.projeto.application.rest.controller;

import com.projeto.application.dto.CriarInspetorDTO;
import com.projeto.application.dto.InspetorResponseDTO;
import com.projeto.application.dto.PresencaResponseDTO;
import com.projeto.application.mapper.InspetorMapper;
import com.projeto.application.mapper.PresencaMapper;
import com.projeto.domain.model.Inspetor;
import com.projeto.domain.model.Presenca;
import com.projeto.domain.model.Role;
import com.projeto.domain.port.in.DiretorUseCase;
import com.projeto.domain.port.in.PresencaUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DiretorControllerTest {

    @Mock
    private DiretorUseCase diretorUseCase;
    @Mock
    private InspetorMapper inspetorMapper;
    @Mock
    private PresencaUseCase presencaUseCase;
    @Mock
    private PresencaMapper presencaMapper;

    @InjectMocks
    private DiretorController diretorController;

    @Nested
    @DisplayName("cadastrarInspetor")
    class CadastrarInspetor {

        @Test
        void retornaResponseCreatedQuandoCadastroValido() {
            CriarInspetorDTO dto = new CriarInspetorDTO("nome", "login", "senha");
            Inspetor inspetor = new Inspetor();
            InspetorResponseDTO responseDTO = new InspetorResponseDTO(1L, "nome", "login");

            when(diretorUseCase.cadastrarInspetor(dto.nome(), dto.login(), dto.senha())).thenReturn(inspetor);
            when(inspetorMapper.toResponseDTO(inspetor)).thenReturn(responseDTO);

            ResponseEntity<InspetorResponseDTO> response = diretorController.cadastrarInspetor(dto);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            assertThat(response.getBody()).isEqualTo(responseDTO);
        }
    }

    @Nested
    @DisplayName("desativarInspetor")
    class DesativarInspetor {

        @Test
        void retornaNoContentQuandoDesativacaoValida() {
            Long id = 1L;

            ResponseEntity<Void> response = diretorController.desativarInspetor(id);

            verify(diretorUseCase).desativarInspetor(id);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
            assertThat(response.getBody()).isNull();
        }
    }

    @Nested
    @DisplayName("verPresencasInspetores")
    class VerPresencasInspetores {

        @Test
        void retornaOkComListaDePresencas() {
            LocalDate data = LocalDate.now();
            Presenca presenca = new Presenca();
            PresencaResponseDTO presencaDTO = new PresencaResponseDTO(1L, data, "login.inspetor");
            when(presencaUseCase.buscarPresencasPorDataERole(data, Role.INSPETOR)).thenReturn(List.of(presenca));
            when(presencaMapper.toResponseDTO(presenca)).thenReturn(presencaDTO);

            ResponseEntity<List<PresencaResponseDTO>> response = diretorController.verPresencasInspetores(data);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().size()).isEqualTo(1);
            assertThat(response.getBody().get(0)).isEqualTo(presencaDTO);
        }

        @Test
        void retornaOkComListaVazia() {
            LocalDate data = LocalDate.now();
            when(presencaUseCase.buscarPresencasPorDataERole(data, Role.INSPETOR)).thenReturn(Collections.emptyList());

            ResponseEntity<List<PresencaResponseDTO>> response = diretorController.verPresencasInspetores(data);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody()).isEmpty();
        }
    }

    @Nested
    @DisplayName("verPresencasProfessores")
    class VerPresencasProfessores {

        @Test
        void retornaOkComListaDePresencas() {
            LocalDate data = LocalDate.now();
            Presenca presenca = new Presenca();
            PresencaResponseDTO presencaDTO = new PresencaResponseDTO(1L, data, "login.professor");
            when(presencaUseCase.buscarPresencasPorDataERole(data, Role.PROFESSOR)).thenReturn(List.of(presenca));
            when(presencaMapper.toResponseDTO(presenca)).thenReturn(presencaDTO);

            ResponseEntity<List<PresencaResponseDTO>> response = diretorController.verPresencasProfessores(data);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().size()).isEqualTo(1);
            assertThat(response.getBody().get(0)).isEqualTo(presencaDTO);
        }
    }
}