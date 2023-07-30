package com.donate.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Bean
    public UserDetailsService userDetailsService(){

//        UserDetails adminuser = User.withUsername("danish")
//                .password(passwordEncoder().encode("danish"))
//                .roles("Admin")
//                .build();
//
//        UserDetails normaluser = User.withUsername("danishkhan")
//                .password(passwordEncoder().encode("vanish"))
//                .roles("Normal")
//                .build();
////
//        return new InMemoryUserDetailsManager(adminuser,normaluser);

        return new CustomUserDetailService();

    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http.
                csrf()
                .disable()
                .authorizeHttpRequests()
                .requestMatchers(   "/home/**")
                .permitAll()
                .and()
                .authorizeHttpRequests()
                .requestMatchers("/home")
                .authenticated()
                .and()
                .formLogin()
                .and()
                .build();


//          return http.build();
//     http.addFilterBefore(this.jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);


    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(this.userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return  authenticationProvider;
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{

        return config.getAuthenticationManager();

    }
}
