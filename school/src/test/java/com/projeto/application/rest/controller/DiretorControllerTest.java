package com.projeto.application.rest.controller;

import com.projeto.application.dto.CriarInspetorDTO;
import com.projeto.application.dto.InspetorResponseDTO;
import com.projeto.application.mapper.InspetorMapper;
import com.projeto.domain.model.Inspetor;
import com.projeto.domain.port.in.DiretorUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DiretorControllerTest {

    @Mock
    private DiretorUseCase diretorUseCase;
    @Mock
    private InspetorMapper inspetorMapper;

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

        @Test
        void lancaExcecaoQuandoDiretorUseCaseFalha() {
            CriarInspetorDTO dto = new CriarInspetorDTO("nome", "login", "senha");

            when(diretorUseCase.cadastrarInspetor(dto.nome(), dto.login(), dto.senha()))
                    .thenThrow(new RuntimeException("Erro ao cadastrar inspetor"));

            assertThatThrownBy(() -> diretorController.cadastrarInspetor(dto))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Erro ao cadastrar inspetor");
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

        @Test
        void lancaExcecaoQuandoDiretorUseCaseFalha() {
            Long id = 1L;

            doThrow(new RuntimeException("Erro ao desativar inspetor"))
                    .when(diretorUseCase).desativarInspetor(id);

            assertThatThrownBy(() -> diretorController.desativarInspetor(id))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Erro ao desativar inspetor");
        }
    }
}