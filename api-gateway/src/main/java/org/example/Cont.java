package org.example;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Cont {
    @GetMapping
    public void te() {
        System.out.println("g");
    }

}

