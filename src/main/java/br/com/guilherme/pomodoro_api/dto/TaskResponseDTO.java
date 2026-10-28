package br.com.guilherme.pomodoro_api.dto;

public record TaskResponseDTO(Long id, String title, boolean completed) {
}