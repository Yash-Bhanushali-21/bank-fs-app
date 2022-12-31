package com.bankapp.config;

import com.bankapp.filter.CustomPreLoginFilter;
import com.bankapp.filter.JWTTokenGeneratorFilter;
import com.bankapp.filter.JWTTokenValidatorFilter;
import com.bankapp.config.CustomOAuth2Service;
import com.bankapp.model.Authority;
import com.nimbusds.oauth2.sdk.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.util.StringUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;


@Configuration
@EnableWebSecurity
public class ProjectSecurityConfig {

    @Autowired
    CustomOAuth2Service customOAuth2Service;

    @Autowired
    NoPopupBasicAuthenticationEntryPoint authenticationEntryPoint;
    @Autowired
    CustomAuthenticationSuccessHandler successHandler;

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        //turning off the default behaviour Jsession ID token in cookie.
      //  http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

               // .and()
        http.
                cors().configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration config = new CorsConfiguration();
                        config.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
                        config.setAllowedMethods(Collections.singletonList("*"));
                        config.setAllowCredentials(true);
                        config.setAllowedHeaders(Collections.singletonList("*"));
                        config.setExposedHeaders(Arrays.asList("Authorization"));
                        config.setMaxAge(3600L);
                        return config;
                    }
                })
                .and()
                .securityContext().requireExplicitSave(false)
                .and()
                .csrf().ignoringAntMatchers("/contact","/user", "/register","/userInfo", "/logout", "/authorization-logout")
            //generate a http (UI) readable cookie for first authenticated req.
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .and()
                //trigger after the login operation. the condition is once when accessing login.
                .addFilterAfter(new JWTTokenGeneratorFilter() , BasicAuthenticationFilter.class)
                .addFilterBefore(new JWTTokenValidatorFilter() , BasicAuthenticationFilter.class)
                .addFilterBefore(new CustomPreLoginFilter() , OAuth2LoginAuthenticationFilter.class)
                .authorizeHttpRequests()
                .antMatchers("/myAccount").hasAuthority("VIEWACCOUNT")
                .antMatchers("/myBalance").hasAnyAuthority("VIEWACCOUNT","VIEWBALANCE")
                .antMatchers("/myLoans").hasAuthority("VIEWLOANS")
                .antMatchers("/myCards").hasAuthority("VIEWCARDS")
                .antMatchers("/authorization-logout" , "/user")
                .authenticated()
                .antMatchers("/notices", "/contact" , "/register", "/authorization-logout").permitAll()
                .and().formLogin().usernameParameter("email").passwordParameter("password")
                .and().httpBasic().authenticationEntryPoint(this.authenticationEntryPoint)
                .and().oauth2Login(userInfo -> {
                            try {
                                userInfo
                                        .userInfoEndpoint().userService(this.customOAuth2Service)
                                        .and().successHandler(this.successHandler)
                                        .and()
                                        .logout(logout -> logout.logoutSuccessHandler(this.oidcLogoutSuccessHandler()))
                                ;

                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }

                )
                .logout().invalidateHttpSession(true).clearAuthentication(true)
                .deleteCookies("JSESSIONID").logoutSuccessHandler(new LogoutSuccessHandler() {
                    @Override
                    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                        System.out.println("Invalidating session..");
                        request.getSession().invalidate();
                    }
                })
        ;

        ;


        return http.build();
    }

    private LogoutSuccessHandler oidcLogoutSuccessHandler() {
        OidcClientInitiatedLogoutSuccessHandler oidcLogoutSuccessHandler = new OidcClientInitiatedLogoutSuccessHandler(this.clientRegistrationRepository);
        oidcLogoutSuccessHandler.setPostLogoutRedirectUri("http://localhost:3000/login");
        return oidcLogoutSuccessHandler;
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
