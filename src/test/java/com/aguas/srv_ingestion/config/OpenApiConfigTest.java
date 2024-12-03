package com.aguas.srv_ingestion.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.junit.jupiter.api.Assertions.*;

public class OpenApiConfigTest {
    
    private final OpenApiConfig openApiConfig = new OpenApiConfig();
    
    @Test
    void customOpenAPI_ShouldReturnCorrectConfiguration() {
        OpenAPI openAPI = openApiConfig.customOpenAPI();
        
        assertNotNull(openAPI);
        assertNotNull(openAPI.getInfo());
        assertEquals("API de Ingestão de Dados", openAPI.getInfo().getTitle());
        assertEquals("API responsável por receber leituras de sensores e publicá-las no Kafka", openAPI.getInfo().getDescription());
        assertEquals("1.0.0", openAPI.getInfo().getVersion());
    }
    
    @Test
    void swaggerRouter_ShouldReturnValidRouterFunction() {
        RouterFunction<ServerResponse> routerFunction = openApiConfig.swaggerRouter();
        
        assertNotNull(routerFunction);
    }
}
