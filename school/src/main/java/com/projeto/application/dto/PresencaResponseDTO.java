package com.projeto.application.dto;

import java.time.LocalDate;

public record PresencaResponseDTO(Long id, LocalDate data, String loginUsuario) {
}