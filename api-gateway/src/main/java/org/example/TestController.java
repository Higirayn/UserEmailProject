package org.example;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    
    @GetMapping("/gateway/health")
    public String health() {
        return "API Gateway is running!";
    }
    
    @GetMapping("/gateway/test")
    public String test() {
        return "Gateway test endpoint working";
    }
} 