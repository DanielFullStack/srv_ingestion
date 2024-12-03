package com.aguas.srv_ingestion.service;

import com.aguas.srv_ingestion.model.PressureReading;
import com.aguas.srv_ingestion.repository.PressureReadingRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IngestionServiceTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Mock
    private PressureReadingRepository repository;

    private IngestionService ingestionService;

    @BeforeEach
    void setUp() {
        ingestionService = new IngestionService(kafkaTemplate);
        ingestionService.repository = repository;
    }

    @Test
    void shouldProcessValidPressureReading() throws JsonProcessingException, InterruptedException {
        PressureReading reading = new PressureReading();
        reading.setSensorId("sensor1");
        reading.setPressure(100.0);
        reading.setReadingDateTime(LocalDateTime.now());

        when(kafkaTemplate.send(eq("pressure-readings"), eq("sensor1"), any(String.class)))
            .thenReturn(null);

        ingestionService.processPressureReading(reading);

        Thread.sleep(100); // Wait for async execution

        verify(repository).save(any(PressureReading.class));
        verify(kafkaTemplate).send(eq("pressure-readings"), eq("sensor1"), any(String.class));
    }

    @Test
    void shouldNotProcessInvalidPressureReading_NullSensorId() throws InterruptedException {
        PressureReading reading = new PressureReading();
        reading.setPressure(100.0);
        reading.setReadingDateTime(LocalDateTime.now());

        ingestionService.processPressureReading(reading);

        Thread.sleep(100); // Wait for async execution

        verify(repository, never()).save(any(PressureReading.class));
        verify(kafkaTemplate, never()).send(anyString(), anyString(), anyString());
    }

    @Test
    void shouldNotProcessInvalidPressureReading_ZeroPressure() throws InterruptedException {
        PressureReading reading = new PressureReading();
        reading.setSensorId("sensor1");
        reading.setPressure(0.0);
        reading.setReadingDateTime(LocalDateTime.now());

        ingestionService.processPressureReading(reading);

        Thread.sleep(100); // Wait for async execution

        verify(repository, never()).save(any(PressureReading.class));
        verify(kafkaTemplate, never()).send(anyString(), anyString(), anyString());
    }

    @Test
    void shouldNotProcessInvalidPressureReading_NegativePressure() throws InterruptedException {
        PressureReading reading = new PressureReading();
        reading.setSensorId("sensor1");
        reading.setPressure(-10.0);
        reading.setReadingDateTime(LocalDateTime.now());

        ingestionService.processPressureReading(reading);

        Thread.sleep(100); // Wait for async execution

        verify(repository, never()).save(any(PressureReading.class));
        verify(kafkaTemplate, never()).send(anyString(), anyString(), anyString());
    }
}
