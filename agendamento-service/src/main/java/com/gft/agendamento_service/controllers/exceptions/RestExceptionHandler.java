package com.gft.agendamento_service.controllers.exceptions;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.gft.agendamento_service.exceptions.ApiIntegrationException;
import com.gft.agendamento_service.exceptions.BusinessException;
import com.gft.agendamento_service.exceptions.ForbbidenException;
import com.gft.agendamento_service.exceptions.ResourceNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException e,
            HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;

        ErrorResponse err = new ErrorResponse(
                Instant.now(),
                status.value(),
                "Recurso não encontrado",
                e.getMessage(),
                request.getRequestURI());

        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        ErrorResponse err = new ErrorResponse(
                Instant.now(),
                status.value(),
                "Requisição inválida",
                e.getMessage(),
                request.getRequestURI());

        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException e,
            HttpServletRequest request) {
        Map<String, String> fieldErrors = new HashMap<>();

        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            fieldErrors.put(fieldName, errorMessage);
        });

        HttpStatus status = HttpStatus.BAD_REQUEST;

        ErrorResponse err = new ErrorResponse(
                Instant.now(),
                status.value(),
                "Erro de validação",
                "Um ou mais campos falharam na validação",
                request.getRequestURI());

        err.setFieldErrors(fieldErrors);

        return ResponseEntity.status(status).body(err);

    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrity(DataIntegrityViolationException e,
            HttpServletRequest request) {

        HttpStatus status = HttpStatus.CONFLICT;
        String errorMessage = "Violação da integridade dos dados";

        Throwable rootCause = e.getRootCause();
        if (rootCause != null) {
            String causeMessage = rootCause.getMessage();
            if (causeMessage != null && causeMessage.contains("Duplicate entry")) {
                errorMessage = "O cpf fornecido já está em uso";
            }
        }
        ErrorResponse err = new ErrorResponse(
                Instant.now(),
                status.value(),
                "Conflito de dados",
                errorMessage,
                request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(ForbbidenException.class)
    public ResponseEntity<ErrorResponse> handleForbbiden(ForbbidenException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.FORBIDDEN;

        ErrorResponse err = new ErrorResponse(
                Instant.now(),
                status.value(),
                "Acesso negado",
                e.getMessage(),
                request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        ErrorResponse err = new ErrorResponse(
                Instant.now(),
                status.value(),
                "Erro de negócio",
                e.getMessage(),
                request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(ApiIntegrationException.class)
    public ResponseEntity<ErrorResponse> handleApiIntegrationException(ApiIntegrationException e,
            HttpServletRequest request) {
        HttpStatus status = HttpStatus.SERVICE_UNAVAILABLE;

        ErrorResponse err = new ErrorResponse(
                Instant.now(),
                status.value(),
                "Erro de integração com API externa",
                e.getMessage(),
                request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        ErrorResponse err = new ErrorResponse(
                Instant.now(),
                status.value(),
                "Erro interno do servidor",
                "Ocorreu um erro inesperado. Contate o administrador",
                request.getRequestURI());

        return ResponseEntity.status(status).body(err);
    }

}
