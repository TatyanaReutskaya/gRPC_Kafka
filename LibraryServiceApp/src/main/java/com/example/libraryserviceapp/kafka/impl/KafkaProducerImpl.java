package com.example.libraryserviceapp.kafka.impl;

import com.example.libraryserviceapp.dto.UserAssignBooksDTO;
import com.example.libraryserviceapp.kafka.KafkaProducer;
import com.example.libraryserviceapp.repository.BookRepository;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KafkaProducerImpl implements KafkaProducer {
    private final KafkaTemplate<String, UserAssignBooksDTO> kafkaTemplate;
    private final BookRepository bookRepository;
    private final Logger LOGGER = LoggerFactory.getLogger("kafka producer");

    @Override
    @Transactional (readOnly = true, propagation = Propagation.REQUIRES_NEW)
    public void reportUserService(UUID userId, int action) {
        int count = bookRepository.countByUserId(userId);
        kafkaTemplate.send("for_user_service", new UserAssignBooksDTO(userId,count+action));
        LOGGER.info("send to topic 'for_user_service' {"+userId+", "+(count+action)+"}");
    }
}
