package com.projeto.application.rest.controller;

import com.projeto.application.dto.CriarProfessorDTO;
import com.projeto.application.dto.ProfessorResponseDTO;
import com.projeto.application.mapper.ProfessorMapper;
import com.projeto.domain.model.Professor;
import com.projeto.domain.port.in.InspetorUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InspetorControllerTest {

    @Mock
    private InspetorUseCase inspetorUseCase;
    @Mock
    private ProfessorMapper professorMapper;

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

        @Test
        void lancaExcecaoQuandoUseCaseFalha() {
            CriarProfessorDTO dto = new CriarProfessorDTO("nome", "login", "senha");

            when(inspetorUseCase.cadastrarProfessor(dto.nome(), dto.login(), dto.senha()))
                    .thenThrow(new RuntimeException("Erro ao cadastrar professor"));

            assertThatThrownBy(() -> controller.cadastrarProfessor(dto))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Erro ao cadastrar professor");
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

        @Test
        void lancaExcecaoQuandoUseCaseFalha() {
            Long id = 1L;

            doThrow(new RuntimeException("Erro ao desativar professor"))
                    .when(inspetorUseCase).desativarProfessor(id);

            assertThatThrownBy(() -> controller.desativarProfessor(id))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Erro ao desativar professor");
        }
    }
}