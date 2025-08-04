package com.projeto.application.rest.controller;

import com.projeto.application.dto.CriarInspetorDTO;
import com.projeto.application.dto.InspetorResponseDTO;
import com.projeto.application.mapper.InspetorMapper;
import com.projeto.domain.model.Inspetor;
import com.projeto.domain.port.in.DiretorUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/diretor")
@PreAuthorize("hasRole('DIRETOR')")
public class DiretorController {

    private final DiretorUseCase diretorUseCase;
    private final InspetorMapper inspetorMapper;

    public DiretorController(DiretorUseCase diretorUseCase, InspetorMapper inspetorMapper) {
        this.diretorUseCase = diretorUseCase;
        this.inspetorMapper = inspetorMapper;
    }

    @PostMapping("/inspetores")
    public ResponseEntity<InspetorResponseDTO> cadastrarInspetor(@RequestBody CriarInspetorDTO dto) {
        Inspetor novoInspetor = diretorUseCase.cadastrarInspetor(dto.nome(), dto.login(), dto.senha());
        InspetorResponseDTO response = inspetorMapper.toResponseDTO(novoInspetor);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/inspetores/{id}")
    public ResponseEntity<Void> desativarInspetor(@PathVariable Long id) {
        diretorUseCase.desativarInspetor(id);
        return ResponseEntity.noContent().build();
    }
}