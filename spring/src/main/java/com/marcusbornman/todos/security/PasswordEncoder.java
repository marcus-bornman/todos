package com.marcusbornman.todos.security;

import com.marcusbornman.todos.model.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RepositoryEventHandler
public class PasswordEncoder {
    @HandleBeforeCreate
    @HandleBeforeSave
    public void handleUserUpdate(User user) {
        if (!StringUtils.isEmpty(user.getPassword())) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
    }
}
