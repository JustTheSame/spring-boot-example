package com.lance.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class MutiSecurityConfig {

    @Configuration
    @Order(1)
    public static class ClientConfigurationAdapter extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {

            http
                    .antMatcher("/v1/**")
                    .formLogin().loginPage("/v1/login").successForwardUrl("/login").failureUrl("/login-error").permitAll()
                    .and()
                    .authorizeRequests().anyRequest().authenticated()
                    .and()
                    .csrf().disable();
        }
    }

    @Configuration
    @Order(2)
    public static class AdminConfigurationAdapter extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {

            http
                    .antMatcher("/v2/**")
                    .formLogin().loginPage("/v2/login").successForwardUrl("/login").failureUrl("/login-error").permitAll()
                    .and()
                    .authorizeRequests().anyRequest().authenticated()
                    .and()
                    .csrf().disable();
        }
    }
}
