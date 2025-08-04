package com.projeto.application.rest.controller;

import com.projeto.application.dto.ApiErrorDTO;
import com.projeto.application.exception.BusinessException;
import com.projeto.application.exception.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler handler;

    @Mock
    private HttpServletRequest request;

    @Nested
    @DisplayName("ResourceNotFoundException")
    class ResourceNotFoundExceptionTests {
        @Test
        void returnsNotFoundWithCorrectDetails() {
            when(request.getRequestURI()).thenReturn("/api/resource/1");
            ResourceNotFoundException ex = new ResourceNotFoundException("Resource not found");

            ResponseEntity<ApiErrorDTO> response = handler.handleResourceNotFoundException(ex, request);

            assertThat(response.getStatusCode().value()).isEqualTo(404);
            assertThat(response.getBody().error()).isEqualTo("Resource Not Found");
            assertThat(response.getBody().message()).isEqualTo("Resource not found");
            assertThat(response.getBody().path()).isEqualTo("/api/resource/1");
        }
    }

    @Nested
    @DisplayName("BusinessException")
    class BusinessExceptionTests {
        @Test
        void returnsBadRequestWithCorrectDetails() {
            when(request.getRequestURI()).thenReturn("/api/business");
            BusinessException ex = new BusinessException("Business rule violated");

            ResponseEntity<ApiErrorDTO> response = handler.handleBusinessException(ex, request);

            assertThat(response.getStatusCode().value()).isEqualTo(400);
            assertThat(response.getBody().error()).isEqualTo("Business Rule Violation");
            assertThat(response.getBody().message()).isEqualTo("Business rule violated");
            assertThat(response.getBody().path()).isEqualTo("/api/business");
        }
    }

    @Nested
    @DisplayName("MethodArgumentNotValidException")
    class MethodArgumentNotValidExceptionTests {
        @Mock
        private BindingResult bindingResult;

        @Test
        void returnsBadRequestWithValidationErrors() {
            when(request.getRequestURI()).thenReturn("/api/validate");
            FieldError fieldError = new FieldError("object", "field", "must not be null");
            when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));
            MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);

            ResponseEntity<ApiErrorDTO> response = handler.handleValidationExceptions(ex, request);

            assertThat(response.getStatusCode().value()).isEqualTo(400);
            assertThat(response.getBody().error()).isEqualTo("Validation Error");
            assertThat(response.getBody().message()).isEqualTo("A validação dos campos falhou");
            assertThat(response.getBody().path()).isEqualTo("/api/validate");
            assertThat(response.getBody().validationErrors()).containsExactly("field: must not be null");
        }

        @Test
        void returnsBadRequestWithEmptyErrorsListWhenNoFieldErrors() {
            when(request.getRequestURI()).thenReturn("/api/validate");
            when(bindingResult.getFieldErrors()).thenReturn(Collections.emptyList());
            MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);

            ResponseEntity<ApiErrorDTO> response = handler.handleValidationExceptions(ex, request);

            assertThat(response.getStatusCode().value()).isEqualTo(400);
            assertThat(response.getBody().validationErrors()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Generic Exception")
    class GenericExceptionTests {
        @Test
        void returnsInternalServerErrorWithExceptionMessage() {
            when(request.getRequestURI()).thenReturn("/api/unknown");
            Exception ex = new Exception("Unexpected error");

            ResponseEntity<ApiErrorDTO> response = handler.handleGlobalException(ex, request);

            assertThat(response.getStatusCode().value()).isEqualTo(500);
            assertThat(response.getBody().error()).isEqualTo("Internal Server Error");
            assertThat(response.getBody().message()).isEqualTo("Unexpected error");
            assertThat(response.getBody().path()).isEqualTo("/api/unknown");
        }
    }
}