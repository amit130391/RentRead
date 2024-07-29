package com.crio.RentRead.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    CustomUserDetailsService userService;

    @Autowired
    CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.csrf(csrf->csrf.disable());

        httpSecurity.authenticationProvider(authenticationProvider());

        // httpSecurity.authorizeHttpRequests(configurer->configurer.
        //             requestMatchers("/register").
        //             permitAll().
        //             anyRequest().
        //             authenticated()
        //             );

        httpSecurity.authorizeHttpRequests(request->request.
                requestMatchers("/register","/error","/login","/logout").
                permitAll().
                requestMatchers("/admin/**").hasRole("ADMIN").
                requestMatchers("/user/**").hasAnyRole("USER", "ADMIN").
                anyRequest().
                authenticated()
                );
    //             .formLogin(form -> form
    //                 .loginPage("/login")
    //                 .loginProcessingUrl("/login")
    //                 .defaultSuccessUrl("/welcome", true)
    //                 .failureUrl("/login?error=true")
    // //                .successHandler(customAuthenticationSuccessHandler)
    //                 .permitAll()
    //             ).logout(logout -> logout
    //             .logoutUrl("/logout")
    //             . logoutSuccessUrl("/login?logout")
    //             .permitAll()
            
            httpSecurity.httpBasic(Customizer.withDefaults());
         return httpSecurity.build();        
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }

    @Bean
    AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider=new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    
}
