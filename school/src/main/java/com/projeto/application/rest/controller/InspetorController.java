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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/inspetor")
@PreAuthorize("hasRole('INSPETOR')")
public class InspetorController {

    private final InspetorUseCase inspetorUseCase;
    private final ProfessorMapper professorMapper;
    private final PresencaUseCase presencaUseCase;
    private final PresencaMapper presencaMapper;

    public InspetorController(InspetorUseCase inspetorUseCase, ProfessorMapper professorMapper, PresencaUseCase presencaUseCase, PresencaMapper presencaMapper) {
        this.inspetorUseCase = inspetorUseCase;
        this.professorMapper = professorMapper;
        this.presencaUseCase = presencaUseCase;
        this.presencaMapper = presencaMapper;
    }

    @PostMapping("/professores")
    public ResponseEntity<ProfessorResponseDTO> cadastrarProfessor(@RequestBody CriarProfessorDTO dto) {
        Professor novoProfessor = inspetorUseCase.cadastrarProfessor(dto.nome(), dto.login(), dto.senha());
        ProfessorResponseDTO response = professorMapper.toResponseDTO(novoProfessor);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/professores/{id}")
    public ResponseEntity<Void> desativarProfessor(@PathVariable Long id) {
        inspetorUseCase.desativarProfessor(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/presenca")
    public ResponseEntity<PresencaResponseDTO> registrarPresencaPropria(@RequestBody RegistrarPresencaDTO dto, @AuthenticationPrincipal Usuario usuario) {
        Presenca presenca = presencaUseCase.registrarPresenca(usuario.getLogin(), dto.data());
        PresencaResponseDTO response = presencaMapper.toResponseDTO(presenca);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/presencas")
    public ResponseEntity<List<PresencaResponseDTO>> verPresencasProprias(@AuthenticationPrincipal Usuario usuario) {
        List<Presenca> presencas = presencaUseCase.buscarPresencasPorUsuario(usuario.getLogin());
        List<PresencaResponseDTO> response = presencas.stream()
                .map(presencaMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/presencas/professores")
    public ResponseEntity<List<PresencaResponseDTO>> verPresencasProfessores(@RequestParam LocalDate data) {
        List<Presenca> presencas = presencaUseCase.buscarPresencasPorDataERole(data, Role.PROFESSOR);
        List<PresencaResponseDTO> response = presencas.stream()
                .map(presencaMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
}