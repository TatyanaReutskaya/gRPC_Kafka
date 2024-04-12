package com.example.libraryserviceapp.config.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfigTopic {
    @Bean
    public NewTopic topic2() {
        return TopicBuilder.name("for_user_service")
                .replicas(2)
                .partitions(3).build();
    }
}
