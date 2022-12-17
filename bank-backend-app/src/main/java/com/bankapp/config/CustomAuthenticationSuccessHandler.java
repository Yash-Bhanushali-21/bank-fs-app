package com.bankapp.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Service
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {



    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        System.out.println("AuthenticationSuccessHandler invoked");
        CustomOAuth2User oauthUser = (CustomOAuth2User) authentication.getPrincipal();


        Collection<? extends GrantedAuthority> authorities = oauthUser.getAuthorities();

        /*

        Map<String, Object> attributes = oauthUser.getAttributes();

        for (Map.Entry<String,Object> entry : attributes.entrySet()){
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());
        }

         for(GrantedAuthority authority : authorities) {
            System.out.println(authority.getAuthority());
        }

       */

       // dbRegisterService.processOAuthPostLogin(name,email);

        //send to the main homepage once done.
        response.sendRedirect("http://localhost:3000/");

        //super.onAuthenticationSuccess(request,response,authentication);

    }

    private  Set<GrantedAuthority> getAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Set<GrantedAuthority> grantedAuthoritySet = new HashSet<GrantedAuthority>();

        for(GrantedAuthority authority : authorities) {
            grantedAuthoritySet.add(authority);
        }
        return grantedAuthoritySet;
    }

}
