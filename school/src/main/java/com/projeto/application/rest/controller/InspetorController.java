package com.projeto.application.rest.controller;

import com.projeto.application.dto.CriarProfessorDTO;
import com.projeto.application.dto.ProfessorResponseDTO;
import com.projeto.application.mapper.ProfessorMapper;
import com.projeto.domain.model.Professor;
import com.projeto.domain.port.in.InspetorUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inspetor")
@PreAuthorize("hasRole('INSPETOR')")
public class InspetorController {

    private final InspetorUseCase inspetorUseCase;
    private final ProfessorMapper professorMapper;

    public InspetorController(InspetorUseCase inspetorUseCase, ProfessorMapper professorMapper) {
        this.inspetorUseCase = inspetorUseCase;
        this.professorMapper = professorMapper;
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
}