package com.bankapp.controller;


import com.bankapp.response.ResponseHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoansController {

    @RequestMapping("/myLoans")
    ResponseEntity<Object> getLoanDetails() {
        return ResponseHandler.generateResponse(HttpStatus.OK,false,"This is loan related stuff",null);
    }
}
