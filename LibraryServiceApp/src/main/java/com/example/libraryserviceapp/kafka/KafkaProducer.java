package com.example.libraryserviceapp.kafka;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface KafkaProducer {
    void reportUserService(UUID userId, int action);
}
