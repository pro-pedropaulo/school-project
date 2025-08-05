package com.projeto.application.rest.controller;

import com.projeto.application.dto.PresencaResponseDTO;
import com.projeto.application.dto.RegistrarPresencaDTO;
import com.projeto.application.mapper.PresencaMapper;
import com.projeto.domain.model.Presenca;
import com.projeto.domain.model.Usuario;
import com.projeto.domain.port.in.PresencaUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/professor")
@PreAuthorize("hasRole('PROFESSOR')")
public class ProfessorController {

    private final PresencaUseCase presencaUseCase;
    private final PresencaMapper presencaMapper;

    public ProfessorController(PresencaUseCase presencaUseCase, PresencaMapper presencaMapper) {
        this.presencaUseCase = presencaUseCase;
        this.presencaMapper = presencaMapper;
    }

    @PostMapping("/presenca")
    public ResponseEntity<PresencaResponseDTO> registrarPresencaPropria(@RequestBody RegistrarPresencaDTO dto, @AuthenticationPrincipal Usuario usuario) {
        Presenca presenca = presencaUseCase.registrarPresenca(usuario.getLogin(), dto.data());
        return new ResponseEntity<>(presencaMapper.toResponseDTO(presenca), HttpStatus.CREATED);
    }

    @GetMapping("/presencas")
    public ResponseEntity<List<PresencaResponseDTO>> verPresencasProprias(@AuthenticationPrincipal Usuario usuario) {
        List<Presenca> presencas = presencaUseCase.buscarPresencasPorUsuario(usuario.getLogin());
        List<PresencaResponseDTO> response = presencas.stream()
                .map(presencaMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
}