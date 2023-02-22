package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/Hello")
@RestController
public class TestEndpoint {

    @GetMapping
    public String sayHello(){
        return """
                Thi 
                is a ("d 
                """;
    }
}
