package com.aguas.srv_ingestion.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aguas.srv_ingestion.model.PressureReading;

public interface PressureReadingRepository extends JpaRepository<PressureReading, Long> {
}