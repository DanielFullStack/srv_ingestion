package com.aguas.srv_ingestion.service;

import com.aguas.srv_ingestion.model.PressureReading;
import com.aguas.srv_ingestion.repository.PressureReadingRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executor;

@Service
public class IngestionService {

    private static final Logger log = LoggerFactory.getLogger(IngestionService.class);
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final Executor executor;

    @Autowired
    private PressureReadingRepository repository;

    public IngestionService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;

        // Configurar o ObjectMapper com suporte para Java 8 Time API
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());

        // Configuração do Thread Pool para tarefas intensivas
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(5);
        taskExecutor.setMaxPoolSize(10);
        taskExecutor.setQueueCapacity(25);
        taskExecutor.setThreadNamePrefix("ValidationExecutor-");
        taskExecutor.initialize();
        this.executor = taskExecutor;
    }

    /**
     * Valida os dados recebidos e publica no tópico Kafka.
     */
    public void processPressureReading(PressureReading reading) {
        executor.execute(() -> {
            try {
                validateReading(reading);
                repository.save(reading);
                log.debug("Leitura de pressão salva para o sensor {} no banco de dados", reading.getSensorId());
        
                String message = objectMapper.writeValueAsString(reading);
                kafkaTemplate.send("pressure-readings", reading.getSensorId(), message);
                log.info("Leitura enviada para Kafka: {}", message);
            } catch (JsonProcessingException e) {
                log.error("Erro ao serializar leitura: {}", e.getMessage());
            } catch (IllegalArgumentException e) {
                log.warn("Leitura inválida: {}", e.getMessage());
            }
        });
    }

    /**
     * Validações básicas de leitura.
     */
    private void validateReading(PressureReading reading) {
        if (reading.getSensorId() == null || reading.getSensorId().isEmpty()) {
            throw new IllegalArgumentException("Sensor ID está ausente.");
        }
        if (reading.getPressure() <= 0) {
            throw new IllegalArgumentException("Pressão deve ser maior que 0.");
        }
    }
}