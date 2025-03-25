package com.backend.backend;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServerController {
    @GetMapping("/")
    public String index() {
        return "Greetings from the Other side!";
    }
}
