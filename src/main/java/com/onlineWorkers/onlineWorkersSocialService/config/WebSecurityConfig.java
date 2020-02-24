package com.onlineWorkers.onlineWorkersSocialService.config;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

   PasswordEncoder passwordEncoder= PasswordEncoderFactories.createDelegatingPasswordEncoder();
  @Autowired
  private DataSource dataSource;
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth     .inMemoryAuthentication()
//                 .withUser("user")
//                 .password(passwordEncoder.encode("pass"))
//                 .roles("USER");
        auth     .jdbcAuthentication()
                 .dataSource(dataSource)
                 .usersByUsernameQuery("select username,password from User where username=?");

    }

     @Override
    protected void configure(HttpSecurity http) throws Exception {
        http    .csrf()
                .disable()
                .authorizeRequests()
                .antMatchers("/social/**").permitAll()
                .and()
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();

    }

}
