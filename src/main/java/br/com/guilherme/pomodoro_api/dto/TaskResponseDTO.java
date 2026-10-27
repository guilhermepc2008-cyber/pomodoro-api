package br.com.guilherme.pomodoro_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TaskResponseDTO(Long id,
                              @NotBlank(message = "O titulo é obrigatório")
                              @Size(min = 3, max = 100, message = "O título deve ter entre 3 e 100 caracteres")
                              String title,
                              boolean completed) {
}