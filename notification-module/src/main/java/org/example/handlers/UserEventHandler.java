package org.example.handlers;

import org.example.entites.config.KafkaTopics;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class UserEventHandler {

     String topic = KafkaTopics.USER_TOPIC.getTopic().intern();


    @KafkaListener(topics = "user-event", groupId = "notification-group")
    public void handleUserEvent(String value) {
        System.out.println(value + " пришло от Producer'а");
    }

}
