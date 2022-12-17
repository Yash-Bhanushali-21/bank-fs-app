package com.bankapp.config;

import com.bankapp.Provider;
import com.bankapp.model.Customer;
import com.bankapp.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.naming.AuthenticationException;
import java.security.AuthProvider;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CustomOAuth2Service extends DefaultOAuth2UserService {

    @Autowired
    CustomerRepository customerRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);
        System.out.println("CustomOAuth2UserService invoked");
        String regId=  userRequest.getClientRegistration().getRegistrationId();
        return new CustomOAuth2User(this.processOAuth2User(userRequest, user));

    }

    private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oauthUser) {
      List<Customer> currentUserList = customerRepository.findByEmail(oauthUser.getAttribute("email"));
      Customer currentCustomer;
      if(currentUserList.size() > 0) {
          this.updateExisitingUser(currentUserList.get(0) , oauthUser);
      }
      else {
          this.registerNewUser(userRequest,currentUserList.get(0) , oauthUser);
      }

      return oauthUser;
    }

    private  void registerNewUser(OAuth2UserRequest userRequest, Customer existingUser, OAuth2User OauthUser) {


            Customer newCustomer = new Customer();
            newCustomer.setProvider(Provider.valueOf(userRequest.getClientRegistration().getRegistrationId()));
            newCustomer.setEmail(OauthUser.getAttribute("email"));
            newCustomer.setName(OauthUser.getName());
            newCustomer.setPwd("");
            newCustomer.setRole("Member");
            newCustomer.setCreateDt(String.valueOf(new Date(System.currentTimeMillis())));
            newCustomer.setMobileNumber("123445567");
             System.out.println("Saved a new Customer");

             customerRepository.save(newCustomer);


    }

    private  void updateExisitingUser(Customer existingUser, OAuth2User OauthUser) {

        existingUser.setName(OauthUser.getName());
        existingUser.setEmail(OauthUser.getAttribute("email"));
         customerRepository.save(existingUser);

    }
}
