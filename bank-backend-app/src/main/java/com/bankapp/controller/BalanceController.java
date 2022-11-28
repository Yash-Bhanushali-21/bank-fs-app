package com.bankapp.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class BalanceController {

    @RequestMapping("/myBalance")
    String getBalanceDetails() {
        return "balance Details";
    }
}
