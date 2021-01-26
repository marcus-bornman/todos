package com.marcusbornman.spring_todos.config;

import com.marcusbornman.spring_todos.services.UserService;
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
                // Allow access to the actuator to everyone.
                .antMatchers("/actuator/**").permitAll()
                // Allow access to the API docs to everyone.
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/webjars/**").permitAll()
                .antMatchers("/swagger-ui/**").permitAll()
                .antMatchers("/v3/**").permitAll()
                // Allow access to the API to everyone. Method level security is applied to controllers.
                .antMatchers("/api/**").permitAll()
                // Allow access to the home page to everyone.
                .antMatchers("/").permitAll()
                // Allow access to the register page to everyone.
                .antMatchers("/register").permitAll()
                // Only authenticated users can access to do lists.
                .antMatchers("/").permitAll()
                // Deny access to everything else.
                .anyRequest().denyAll()
                // Configure login through the UI and allow access to everyone.
                .and()
                    .formLogin()
                    .loginPage("/login")
                    .loginProcessingUrl("/login")
                    .permitAll()
                // Allow everyone to logout.
                .and()
                .logout().permitAll()
                // Configure HTTP Basic Authentication.
                .and()
                .httpBasic()
                // Disable CSRF
                .and()
                .csrf().disable();
    }
}
