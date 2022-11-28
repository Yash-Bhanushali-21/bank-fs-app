package com.bankapp.controller;

import com.bankapp.model.Customer;
import com.bankapp.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.util.List;


@RestController
public class LoginController {
    @Autowired
     CustomerRepository customerRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Customer customer) {
        Customer savedCustomer =  null;
        ResponseEntity response = null;
        try {
            //creating a password hash of the provided password.
            String hashedPwd = passwordEncoder.encode(customer.getPwd());
            customer.setPwd(hashedPwd);
            //setting current time in milliseconds as Date Object in String.
            customer.setCreateDt(String.valueOf(new Date(System.currentTimeMillis())));

            //saving in the DB.
            savedCustomer = customerRepository.save(customer);
            if(savedCustomer.getId() > 0) {
                response = ResponseEntity.status(HttpStatus.CREATED).body("Given user details are saved");
            }
        }
        catch (Exception ex) {
            response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("an exception occured due to " + ex.getMessage());

        }
        return  response;
    }

    @GetMapping("/user")
    public Customer getUserAfterLogin(Authentication authentication) {
        List<Customer> customers = customerRepository.findByEmail(authentication.getName());
        if (customers.size() > 0) {
            return customers.get(0);
        } else {
            return null;
        }
    }


}
