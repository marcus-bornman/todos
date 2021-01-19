package com.marcusbornman.todos.security;

import com.marcusbornman.todos.data.TodoListRepository;
import com.marcusbornman.todos.data.TodoRepository;
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
    private final UserDetailsService userDetailsService;

    private final TodoListRepository todoListRepository;

    private final TodoRepository todoRepository;

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
                // We are currently allowing access to the actuator to everyone.
                .antMatchers("/actuator/**").permitAll()
                // We also allow full access to the Spring Data REST Api; but, method-level security is employed in the repositories to impose fine-grained control.
                .antMatchers("/api/**").permitAll()
                // We deny all requests other than the ones above.
                .anyRequest().denyAll()
                .and()
                .httpBasic()
                .and()
                .csrf().disable();
    }
}
