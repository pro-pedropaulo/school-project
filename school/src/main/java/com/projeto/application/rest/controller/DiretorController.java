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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/diretor")
@PreAuthorize("hasRole('DIRETOR')")
public class DiretorController {

    private final DiretorUseCase diretorUseCase;
    private final InspetorMapper inspetorMapper;
    private final PresencaUseCase presencaUseCase;
    private final PresencaMapper presencaMapper;

    public DiretorController(DiretorUseCase diretorUseCase, InspetorMapper inspetorMapper, PresencaUseCase presencaUseCase, PresencaMapper presencaMapper) {
        this.diretorUseCase = diretorUseCase;
        this.inspetorMapper = inspetorMapper;
        this.presencaUseCase = presencaUseCase;
        this.presencaMapper = presencaMapper;
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

    @GetMapping("/presencas/inspetores")
    public ResponseEntity<List<PresencaResponseDTO>> verPresencasInspetores(@RequestParam LocalDate data) {
        List<Presenca> presencas = presencaUseCase.buscarPresencasPorDataERole(data, Role.INSPETOR);
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

    @GetMapping("/inspetores/ativos")
    public ResponseEntity<List<InspetorResponseDTO>> listarInspetoresAtivos() {
        List<Inspetor> inspetores = diretorUseCase.listarInspetoresAtivos();
        List<InspetorResponseDTO> response = inspetores.stream()
                .map(inspetorMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
}