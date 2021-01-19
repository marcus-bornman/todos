package com.marcusbornman.todos.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.marcusbornman.todos.model.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class UserDetails implements org.springframework.security.core.userdetails.UserDetails {
    @Getter
    private final User user;

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return LocalDateTime.now().isBefore(user.getExpiryDate());
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return !user.getLocked();
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return LocalDateTime.now().isBefore(user.getCredentialsExpiryDate());
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return !user.getDisabled();
    }
}
