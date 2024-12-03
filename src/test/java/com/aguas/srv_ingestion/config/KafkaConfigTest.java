package com.aguas.srv_ingestion.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import static org.junit.jupiter.api.Assertions.*;

public class KafkaConfigTest {

    private final KafkaConfig kafkaConfig = new KafkaConfig();

    @Test
    public void testProducerFactory() {
        ProducerFactory<String, String> producerFactory = kafkaConfig.producerFactory();

        assertNotNull(producerFactory);
        assertTrue(producerFactory instanceof DefaultKafkaProducerFactory);

        DefaultKafkaProducerFactory<String, String> defaultProducerFactory = (DefaultKafkaProducerFactory<String, String>) producerFactory;
        
        assertEquals("kafka:9092", defaultProducerFactory.getConfigurationProperties().get(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG));
        assertEquals(StringSerializer.class, defaultProducerFactory.getConfigurationProperties().get(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG));
        assertEquals(StringSerializer.class, defaultProducerFactory.getConfigurationProperties().get(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG));
    }

    @Test
    public void testKafkaTemplate() {
        KafkaTemplate<String, String> kafkaTemplate = kafkaConfig.kafkaTemplate();

        assertNotNull(kafkaTemplate);
        assertNotNull(kafkaTemplate.getProducerFactory());
        assertTrue(kafkaTemplate.getProducerFactory() instanceof DefaultKafkaProducerFactory);
    }
}
