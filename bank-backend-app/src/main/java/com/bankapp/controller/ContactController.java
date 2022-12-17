package com.bankapp.controller;

import org.springframework.web.bind.annotation.RequestMapping;

public class ContactController {


    @RequestMapping("/contact")
    String saveContactInquiryDetails() {
        return "save contacts";
    }
}
