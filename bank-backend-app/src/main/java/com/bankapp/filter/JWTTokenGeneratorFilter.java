package com.bankapp.filter;

import com.bankapp.constants.ApplicationConstants;
import com.bankapp.constants.SecurityConstants;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class JWTTokenGeneratorFilter extends OncePerRequestFilter {


    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException , IOException{

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null) {
            //generating a secret key with custom signed key.
            SecretKey key = Keys.hmacShaKeyFor(SecurityConstants.JWT_Key.getBytes(StandardCharsets.UTF_8));
            String jwt = Jwts.builder().setIssuer(ApplicationConstants.APPLICATION_NAME)
                    .setSubject(ApplicationConstants.APPLICATION_SUBJECT)
                    .claim("username" , authentication.getName())
                    .claim("authorities" , this.populateAuthorities(authentication.getAuthorities()))
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(new Date().getTime() + 300000))
                    .signWith(key).compact();
            response.setHeader(SecurityConstants.JWT_HEADER , jwt);


            //proceed with rest of the filter executions in-line.
            filterChain.doFilter(request,response);

        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

        return !request.getServletPath().equals("/user");
    }

    private String populateAuthorities(Collection<? extends GrantedAuthority> collection) {
        Set<String> authoritiesSet = new HashSet<>();
        for (GrantedAuthority authority : collection) {
            authoritiesSet.add(authority.getAuthority());
        }
        //get a comma-sepetated str.
        return String.join(",", authoritiesSet);
    }



}
