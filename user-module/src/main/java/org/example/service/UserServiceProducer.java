package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.entites.config.KafkaTopics;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceProducer {

    private static final String USER_TOPIC = KafkaTopics.USER_TOPIC.getTopic();

    private final KafkaTemplate<String, String> kafkaTemplate;



    public void publishUserCreated(String email) {
        String message = "CREATE: " + email;
        kafkaTemplate.send(USER_TOPIC, message);
    }


    public void publishUserDeleted(String email) {
        String message = "DELETE: " + email;
        kafkaTemplate.send(USER_TOPIC, message);
    }

}
