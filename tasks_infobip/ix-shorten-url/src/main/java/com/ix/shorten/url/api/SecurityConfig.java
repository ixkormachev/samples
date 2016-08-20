package com.ix.shorten.url.api;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.stereotype.Component;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity
//@Component 
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  
  @Autowired
  DataSource dataSource;
  
  @Autowired
  public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
    auth.jdbcAuthentication().dataSource(dataSource)
    .usersByUsernameQuery(
      "select username, password from Account where username=?")
    .authoritiesByUsernameQuery(
        "select username, role from Account where username=?");
    
  }

  
  @Override
  protected void configure(HttpSecurity http) throws Exception {
//    http.authorizeRequests()
//    .antMatchers("/register/**").access("hasRole('ROLE_ADMIN')")
//    .and()
//    .exceptionHandling().accessDeniedPage("/403")
//    .and()
//    .httpBasic();
    
//    http.csrf().disable()
//    .antMatcher("/register/**")
//    .authorizeRequests()
//        .anyRequest().hasAnyRole("ADMIN", "API")
//        .and()
//    .httpBasic();
//    
    
    http.authorizeRequests()
    .antMatchers("/register").access("hasRole('ROLE_ADMIN')")  
    .anyRequest().permitAll()
    .and()
      .formLogin().loginPage("/login")
      .usernameParameter("username").passwordParameter("password")
    .and()
      .logout().logoutSuccessUrl("/login?logout") 
     .and()
     .exceptionHandling().accessDeniedPage("/403")
    .and()
      .csrf();
    
    
 //   http.csrf().disable();


    
    //    http
//    .authorizeRequests()
//        .anyRequest().authenticated()
//        .and()
//    .formLogin().loginPage("/account")
//    .permitAll()
//        .and()
//    .logout()
//    .permitAll();
    
//    http.authorizeRequests()
//    .antMatchers("/accounts/**").access("hasRole('ROLE_ADMIN')");
//    http.authorizeRequests()
//    .antMatchers("/register/**").access("hasRole('ROLE_USER')");
  }
}