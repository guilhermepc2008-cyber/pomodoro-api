package br.com.guilherme.pomodoro_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Pomodoro - API")
                        .description("API para gerenciamento de tarefas do sistema Pomodoro, permitindo criar, listar, atualizar e excluir tarefas, além de marcar como concluída ou pendente.")
                        .version("1.0.0"));
    }
}