//package com.example.demo;
//
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.crypto.password.NoOpPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.DefaultSecurityFilterChain;
//
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
//    private final static org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager.getLogger(LoopitisApplication.MY_LOGGER);
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return NoOpPasswordEncoder.getInstance();
//    }
//
//    @Override
//    public void configure(HttpSecurity http) throws Exception {
//        log.debug("Configuring security...");
//        http
//                .authorizeRequests()
//                .anyRequest().authenticated()
//                .and()
//                .httpBasic();
//    }
//
//
//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        log.debug("Configuring global security...");
//
//        auth
//                .inMemoryAuthentication()
//                .withUser("user").password("password").roles("USER");
//    }
//}