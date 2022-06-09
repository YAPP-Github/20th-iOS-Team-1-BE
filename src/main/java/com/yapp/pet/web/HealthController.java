package com.yapp.pet.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/health-check")
    public ResponseEntity<HttpStatus> healthCheck(){
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
