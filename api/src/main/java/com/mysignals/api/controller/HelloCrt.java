package com.mysignals.api.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HelloCrt {
    
    @GetMapping("/hello")
    public String hello() {
        return "{\"data\" : \"Hello world !\"}";
    }
}