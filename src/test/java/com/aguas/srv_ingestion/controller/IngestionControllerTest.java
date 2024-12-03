package com.aguas.srv_ingestion.controller;

import com.aguas.srv_ingestion.model.PressureReading;
import com.aguas.srv_ingestion.service.IngestionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

public class IngestionControllerTest {
    
    @Mock
    private IngestionService ingestionService;
    
    @InjectMocks
    private IngestionController ingestionController;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    void receberLeitura_ShouldReturnAccepted() {
        // Arrange
        PressureReading reading = new PressureReading();
        doNothing().when(ingestionService).processPressureReading(reading);
        
        // Act
        Mono<ResponseEntity<Void>> result = ingestionController.receberLeitura(reading);
        
        // Assert
        StepVerifier.create(result)
            .expectNext(new ResponseEntity<>(HttpStatus.ACCEPTED))
            .verifyComplete();
            
        verify(ingestionService, times(1)).processPressureReading(reading);
    }
}
