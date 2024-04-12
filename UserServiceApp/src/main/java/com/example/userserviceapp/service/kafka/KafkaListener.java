package com.example.userserviceapp.service.kafka;

import com.example.userserviceapp.dto.UserAssignBooksDTO;
import com.example.userserviceapp.entity.User;
import com.example.userserviceapp.entity.enums.UserStatus;
import com.example.userserviceapp.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@AllArgsConstructor
public class KafkaListener {
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final int MAX_COUNT_BOOKS = 2;
    private final Logger LOGGER = LoggerFactory.getLogger("kafka client");
    @org.springframework.kafka.annotation.KafkaListener(topics = "for_user_service", groupId = "my-group")
    @SneakyThrows
    @Transactional
    public void listen(ConsumerRecord<String,String> record){
        UserAssignBooksDTO dto = objectMapper.readValue(record.value(),UserAssignBooksDTO.class);
        LOGGER.info("pull from topic 'for_user_service' {"+dto.getUserId()+", "+dto.getCountBook()+"}");
        Optional<User> userOptional = userRepository.findById(dto.getUserId());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            int count = dto.getCountBook();
            if (count<MAX_COUNT_BOOKS && user.getUserStatus().equals(UserStatus.BLOCKED)) {
                user.setUserStatus(UserStatus.ACTIVE);
            }
            else if(count>=MAX_COUNT_BOOKS && user.getUserStatus().equals(UserStatus.ACTIVE)) {
                user.setUserStatus(UserStatus.BLOCKED);
            }
        }

    }
}
