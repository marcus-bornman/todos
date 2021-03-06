package com.marcusbornman.todos.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Entity
@Data
public class User implements UserDetails {

  @Id
  @NotNull
  @NotEmpty
  private String username;

  @NotNull
  @NotEmpty
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private String password;

  @NotNull
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private LocalDateTime expiryDate = LocalDateTime.of(9999, 12, 31, 23, 59, 59);

  @NotNull
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Boolean locked = false;

  @NotNull
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private LocalDateTime credentialsExpiryDate = LocalDateTime.of(9999, 12, 31, 23, 59, 59);

  @NotNull
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Boolean disabled = false;

  @NotNull
  @CreationTimestamp
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private LocalDateTime creationDate = LocalDateTime.now();

  @ElementCollection(fetch = FetchType.EAGER)
  @JsonIgnore
  private Set<String> roles;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JsonIgnore
  private List<TodoList> todoLists;

  /**
   * We have implemented a custom setter for the password field. Now, every time the password is set
   * it will have already been encrypted.
   *
   * @param password the unencrypted password.
   */
  public void setPassword(String password) {
    if (!StringUtils.isEmpty(password)) {
      BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
      this.password = passwordEncoder.encode(password);
    }
  }

  @Override
  @JsonIgnore
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
  }

  @Override
  @JsonIgnore
  public boolean isAccountNonExpired() {
    return LocalDateTime.now().isBefore(expiryDate);
  }

  @Override
  @JsonIgnore
  public boolean isAccountNonLocked() {
    return !locked;
  }

  @Override
  @JsonIgnore
  public boolean isCredentialsNonExpired() {
    return LocalDateTime.now().isBefore(credentialsExpiryDate);
  }

  @Override
  @JsonIgnore
  public boolean isEnabled() {
    return !disabled;
  }
}
