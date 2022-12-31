package com.bankapp.filter;

import com.bankapp.constants.ApplicationConstants;
import com.bankapp.constants.SecurityConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class JWTTokenValidatorFilter extends OncePerRequestFilter {
    private static final String[] AUTH_WHITELIST = {
            // -- Swagger UI v2
            "/swagger-ui/index.html",
            "/swagger-ui/swagger-ui.css",
            "/swagger-ui/index.css",
            "/swagger-ui/swagger-ui-bundle.js",
            "/swagger-ui/swagger-ui-standalone-preset.js",
            "/swagger-ui/swagger-initializer.js",
            "/swagger-ui/swagger-ui.css.map",
            "/swagger-ui/swagger-ui-bundle.js.map",
            "/swagger-ui/swagger-ui-standalone-preset.js.map",
            "/v3/api-docs/swagger-config",
            "/swagger-ui/favicon-32x32.png",
            "/v3/api-docs",

    };
    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String jwt = request.getHeader(SecurityConstants.JWT_HEADER);
        if(jwt != null) {
            try {
                SecretKey key = Keys.hmacShaKeyFor(SecurityConstants.JWT_Key.getBytes(StandardCharsets.UTF_8));
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(jwt)
                        .getBody();
                String username=  String.valueOf(claims.get("username"));
                String authorities = (String) claims.get("authorities");
                System.out.println("user name is " + username);
                Authentication auth = new UsernamePasswordAuthenticationToken(username, null,
                        AuthorityUtils.commaSeparatedStringToAuthorityList(authorities));
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
            catch(Exception e) {
                throw new BadCredentialsException("Invalid Token Intercepted!");
            }

            filterChain.doFilter(request,response);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        //do not execute this filter if the path tries to access user.
        String path = request.getServletPath();
        List ignoringList = Arrays.asList(AUTH_WHITELIST);
        boolean shouldNotProceedCondition = ignoringList.contains(path);

        return request.getServletPath().equals("/user") || request.getServletPath().equals("/authorization-logout") || shouldNotProceedCondition ;
    }
}
