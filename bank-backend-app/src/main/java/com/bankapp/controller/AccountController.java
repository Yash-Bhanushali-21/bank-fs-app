package com.bankapp.controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {
    @RequestMapping("/myAccount")
    String getAccountDetails() {
        return "account Details";
    }
}
