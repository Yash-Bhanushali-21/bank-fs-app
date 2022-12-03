package com.bankapp.config;

import com.bankapp.filter.JWTTokenGeneratorFilter;
import com.bankapp.filter.JWTTokenValidatorFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;


@Configuration
public class ProjectSecurityConfig {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        //turning off the default behaviour Jsession ID token in cookie.
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .cors().configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration config = new CorsConfiguration();
                        config.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
                        config.setAllowedMethods(Collections.singletonList("*"));
                        config.setAllowCredentials(true);
                        config.setAllowedHeaders(Collections.singletonList("*"));
                        //expose this header as authorization so that its readable.
                        config.setExposedHeaders(Arrays.asList("Authorization"));
                        config.setMaxAge(3600L);
                        return config;
                    }
                })
                .and()
                .securityContext().requireExplicitSave(false)
                .and()
                //Ignore the CSRF Config for the below routes.
                .csrf().ignoringAntMatchers("/contact", "/register")
                //generate a http (UI) readable cookie for first authenticated req.
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .and()
        //trigger after the login operation. the condition is once when accessing login.
                .addFilterAfter(new JWTTokenGeneratorFilter() , BasicAuthenticationFilter.class)
                .addFilterBefore(new JWTTokenValidatorFilter() , BasicAuthenticationFilter.class)
                .authorizeHttpRequests()
                .antMatchers("/myAccount").hasAuthority("VIEWACCOUNT")
                .antMatchers("/myBalance").hasAnyAuthority("VIEWACCOUNT","VIEWBALANCE")
                .antMatchers("/myLoans").hasAuthority("VIEWLOANS")
                .antMatchers("/myCards").hasAuthority("VIEWCARDS")
                .antMatchers( "/user").authenticated()
                .antMatchers("/notices", "/contact" , "/register").permitAll()
                .and().formLogin()
                .and().httpBasic()
        ;
        return http.build();
    }
    //creating an admin acc. in memory of the spring boot app.

    /*
    @Bean
    public UserDetailsService users() {

        UserDetails defaultUser = User.withUsername("adminaccount")
                .password("adminpassword")
                .authorities("ADMIN").build();

        return new InMemoryUserDetailsManager(defaultUser);
    }

    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource) {
        return new JdbcUserDetailsManager(dataSource);

    }
*/

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
