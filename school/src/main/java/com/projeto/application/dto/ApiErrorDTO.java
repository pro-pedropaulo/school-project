package com.projeto.application.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ApiErrorDTO(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path,
        List<String> validationErrors
) {}