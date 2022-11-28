package com.bankapp.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CardsController {
    @RequestMapping("/myCards")
    String getCardDetails() {
        return "card details";
    }
}
