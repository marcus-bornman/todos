package com.marcusbornman.spring_todos.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class User {
    @Id
    private String username;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotNull
    private LocalDateTime expiryDate = LocalDateTime.of(9999, 12, 31, 23, 59, 59);

    @NotNull
    private Boolean locked = false;

    @NotNull
    private LocalDateTime credentialsExpiryDate = LocalDateTime.of(9999, 12, 31, 23, 59, 59);

    @NotNull
    private Boolean disabled = false;

    @CreationTimestamp
    private LocalDateTime creationDate;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TodoList> todoLists;

    public void setPassword(String password) {
        if (!StringUtils.isEmpty(password)) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            this.password = passwordEncoder.encode(password);
        }
    }
}
