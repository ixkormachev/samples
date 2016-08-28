package com.ix.shorten.url.api;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.ix.shorten.url.service.AuthenticationService;

@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity
//@Component 
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  
  @Autowired
  private DataSource dataSource;


  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
      auth.jdbcAuthentication().dataSource(dataSource)
              .usersByUsernameQuery("select username, password, enabled from Account where username=?")
              .authoritiesByUsernameQuery("select username, authority from Account where username = ?");
     // auth.jdbcAuthentication().set
  }
  
  @Override
  protected void configure(HttpSecurity http) throws Exception {
//    http.authorizeRequests()
//    .antMatchers("/register/**").access("hasRole('ROLE_ADMIN')")
//    .and()
//    .exceptionHandling().accessDeniedPage("/403")
//    .and()
//    .httpBasic();
    
    http.httpBasic().and().csrf().disable().authorizeRequests().antMatchers("/register/**").authenticated(); //.and().httpBasic();
  //  .antMatcher("/register/**")
//    .authorizeRequests().and().httpBasic();//.csrf().disable();
 //   .antMatchers("/account/**", "/**").permitAll();
//    .anyRequest().permitAll()
//    .authorizeRequests().hasAnyRole("ROLE_USER", "API") 
   // .anonymous();
  //      .anyRequest(); //.hasAnyRole("ROLE_USER", "API")
//        .and()
//    .httpBasic();
//    
    
//    http.authorizeRequests()
//    .antMatchers("/register*")  //.access("hasRole('ROLE_ADMIN')");  
//    http.authorizeRequests().anyRequest().access("hasRole('ROLE_ADMIN')").and().
//    httpBasic().and().
//    csrf().disable(); 
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