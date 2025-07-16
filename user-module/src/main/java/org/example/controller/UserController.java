package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.config.KafkaConfig;
import org.example.service.UserServiceProducer;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserServiceProducer producer;


    @PostMapping
    public String createUser(@RequestParam String email) {
        producer.publishUserCreated(email);
        return "User created successfully: " + email;
    }

    @DeleteMapping
    public String deleteUser(@RequestParam String email) {
        producer.publishUserDeleted(email);
        return "User deleted successfully: " + email;
    }



}
