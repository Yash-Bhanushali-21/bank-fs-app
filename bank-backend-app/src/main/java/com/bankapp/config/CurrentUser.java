package com.bankapp.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;


import java.util.Collection;
import java.util.Set;


public class CurrentUser extends User {

    private String email;

    public CurrentUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {

        super(username,password,authorities);
        this.setEmail(username);
    }

    public String getEmail() {
        return this.email;
    }
    public void setEmail(String email) {
        this.email = email;
    }





}
