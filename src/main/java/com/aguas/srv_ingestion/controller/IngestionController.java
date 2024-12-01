package com.aguas.srv_ingestion.controller;

import com.aguas.srv_ingestion.model.PressureReading;
import com.aguas.srv_ingestion.service.IngestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/ingestion")
@RequiredArgsConstructor
public class IngestionController {

    private final IngestionService ingestionService;

    /**
     * Endpoint para receber leituras de pressão.
     */
    @PostMapping("/sensors")
    public Mono<ResponseEntity<Void>> receberLeitura(@RequestBody PressureReading reading) {
        return Mono.fromRunnable(() -> ingestionService.processPressureReading(reading))
                   .then(Mono.just(new ResponseEntity<>(HttpStatus.ACCEPTED)));
    }
}
