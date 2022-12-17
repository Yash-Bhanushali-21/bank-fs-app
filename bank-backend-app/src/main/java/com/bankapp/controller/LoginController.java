package com.bankapp.controller;

import com.bankapp.Provider;
import com.bankapp.config.CustomOAuth2User;
import com.bankapp.model.Customer;
import com.bankapp.repository.CustomerRepository;
import com.bankapp.response.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.util.Collections;
import java.util.List;
import java.util.Map;


@RestController
public class LoginController {
    @Autowired
     CustomerRepository customerRepository;


    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

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
            customer.setProvider(Provider.LOCAL);

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

    @GetMapping("/checkAuthenticated")
    public Map<String, Object> user(Authentication authentication) {

        OidcUser oauthUser = (OidcUser) authentication.getPrincipal();
        if(oauthUser != null) {
            return Collections.singletonMap("isAuthenticated", oauthUser.getEmail());

        }
        return Collections.singletonMap("Error", "Something Went Wrong!");

    }


    @GetMapping("/authorization-logout")
    public ResponseEntity<String> loguserOutFromAuthorizationServer(Authentication authentication) throws Exception {

        try {
            OAuth2AuthorizedClient authorizedClient =
                    this.authorizedClientService.loadAuthorizedClient("google", authentication.getName());

            //revoking the oauth2 accesstoken for google logged in user.
            OAuth2AccessToken accessToken = authorizedClient.getAccessToken();
            String url = "https://oauth2.googleapis.com/revoke?token=".concat(accessToken.getTokenValue());
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.valueOf(MediaType.APPLICATION_FORM_URLENCODED_VALUE));

            HttpEntity<String> entity = new HttpEntity<>(headers);

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.postForLocation(url, entity);
            return  new ResponseEntity<String>(HttpStatus.OK);
        }
        catch(Exception e) {
            System.out.println("Something went wrong");
            return  new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);

        }


    }


    @GetMapping("/user")
    public ResponseEntity<Object> getUserAfterLogin(HttpServletRequest request, Authentication authentication) {

          List<Customer> customers = customerRepository.findByEmail(authentication.getName());
             if (customers.size() > 0) {
                 return  ResponseHandler.generateResponse(HttpStatus.OK, false , "Found Authenticated User",  customers.get(0));
             }
             else {

                 System.out.println("Something went wrong");
                 return  ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, true, "User Not Found", new Object());

             }




    }





}
