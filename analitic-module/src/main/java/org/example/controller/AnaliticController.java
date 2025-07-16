package org.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/analytics")
public class AnaliticController {


    @GetMapping("/get")
    public String analytic() throws InterruptedException {

        System.out.println("Система начала свою аналитику...");

        Thread.sleep(5000);

        System.out.println("Система завершила свою работу успешно");

        return "Success";
    }

}
