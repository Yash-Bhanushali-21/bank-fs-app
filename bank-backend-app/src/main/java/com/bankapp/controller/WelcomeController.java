package com.bankapp.controller;

import com.bankapp.model.Customer;
import com.bankapp.repository.CustomerRepository;
import com.bankapp.response.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class WelcomeController {

    @Autowired
    CustomerRepository customerRepository;

    @GetMapping("/welcome")
    public ResponseEntity<Object> welcome() {
        return ResponseHandler.generateResponse(HttpStatus.CREATED , false,"result",customerRepository.findAll());
    }
}
