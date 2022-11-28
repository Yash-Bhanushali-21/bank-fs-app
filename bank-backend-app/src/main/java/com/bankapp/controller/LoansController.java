package com.bankapp.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoansController {

    @RequestMapping("/myLoans")
    String getLoanDetails() {
        return "loans";
    }
}
