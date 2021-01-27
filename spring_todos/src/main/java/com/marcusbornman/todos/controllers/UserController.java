package com.marcusbornman.todos.controllers;

import com.marcusbornman.todos.entities.User;
import com.marcusbornman.todos.exceptions.UserNotFoundException;
import com.marcusbornman.todos.exceptions.UsernameExistsException;
import com.marcusbornman.todos.services.UserService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "Users")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

  private final UserService userService;

  @Operation(summary = "Add a new User")
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  User postUser(@Validated @RequestBody User user) throws UsernameExistsException {
    return userService.create(user);
  }

  @Operation(
      summary = "Read the details of a user with a specific username",
      security = @SecurityRequirement(name = "Basic Authentication")
  )
  @GetMapping("/{username}")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isFullyAuthenticated() and #username == principal.username")
  User getUser(@PathVariable String username) throws UserNotFoundException {
    return userService.read(username);
  }

  @Operation(
      summary = "Update the details of a user with a specific username",
      security = @SecurityRequirement(name = "Basic Authentication")
  )
  @PutMapping("/{username}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize("isFullyAuthenticated() and #username == principal.username")
  void updateUser(@PathVariable String username, @Validated @RequestBody User user)
      throws UserNotFoundException {
    userService.update(username, user);
  }

  @Operation(
      summary = "Update the details of a user with a specific username",
      security = @SecurityRequirement(name = "Basic Authentication")
  )
  @DeleteMapping("/{username}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize("isFullyAuthenticated() and #username == principal.username")
  void deleteUser(@PathVariable String username) throws UserNotFoundException {
    userService.delete(username);
  }
}
