package br.com.guilherme.pomodoro_api.exception;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.validation.ConstraintViolationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroValidacaoResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        List<ErroCampo> erros = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(erro -> new ErroCampo(
                        erro.getField(),
                        erro.getDefaultMessage()))
                .collect(Collectors.toList());

        ErroValidacaoResponse response = new ErroValidacaoResponse(erros);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErroValidacaoResponse> handleConstraintViolation(ConstraintViolationException ex) {
        List<ErroCampo> erros = ex.getConstraintViolations()
                .stream()
                .map(violacao -> new ErroCampo(
                        violacao.getPropertyPath().toString(),
                        violacao.getMessage()))
                .collect(Collectors.toList());

        ErroValidacaoResponse response = new ErroValidacaoResponse(erros);
        return ResponseEntity.badRequest().body(response);
    }

    public record ErroCampo(String campo, String mensagem) {
    }

    public record ErroValidacaoResponse(List<ErroCampo> erros) {
    }
}