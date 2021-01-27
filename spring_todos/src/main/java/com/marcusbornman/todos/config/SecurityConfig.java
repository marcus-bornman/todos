package com.marcusbornman.todos.config;

import com.marcusbornman.todos.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final UserService userDetailsService;

  @Override
  protected void configure(AuthenticationManagerBuilder auth) {
    DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
    authenticationProvider.setUserDetailsService(userDetailsService);
    authenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder());
    auth.authenticationProvider(authenticationProvider);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
        // We allow access to all endpoints. Method-level security secures specific endpoints.
        .anyRequest().permitAll()
        // Configure login through the UI and allow access to everyone.
        .and().formLogin().loginPage("/login").loginProcessingUrl("/login").permitAll()
        // Allow everyone to logout.
        .and().logout().logoutSuccessUrl("/").permitAll()
        // Configure HTTP Basic Authentication.
        .and().httpBasic()
        // Disable CSRF
        .and().csrf().disable();
  }
}
