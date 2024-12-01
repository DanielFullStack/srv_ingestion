package com.aguas.srv_ingestion.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

import java.net.URI;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Ingestão de Dados")
                        .description("API responsável por receber leituras de sensores e publicá-las no Kafka")
                        .version("1.0.0"));
    }

    @Bean
    public RouterFunction<ServerResponse> swaggerRouter() {
        return RouterFunctions.route()
                .GET("/swagger-ui.html",
                        request -> ServerResponse.permanentRedirect(URI.create("/swagger-ui/index.html")).build())
                .GET("/api-docs/**",
                        request -> ServerResponse.permanentRedirect(URI.create("/api-docs/swagger-config")).build())
                .build();
    }
}
