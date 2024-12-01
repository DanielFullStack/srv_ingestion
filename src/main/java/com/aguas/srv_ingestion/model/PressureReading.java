package com.aguas.srv_ingestion.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PressureReading {
    private String sensorId;
    private double pressure;
    private LocalDateTime readingDateTime;
}