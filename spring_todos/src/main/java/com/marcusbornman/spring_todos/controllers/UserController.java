package com.marcusbornman.spring_todos.controllers;

import com.marcusbornman.spring_todos.entities.User;
import com.marcusbornman.spring_todos.exceptions.UserNotFoundException;
import com.marcusbornman.spring_todos.exceptions.UsernameExistsException;
import com.marcusbornman.spring_todos.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    void updateUser(@PathVariable String username, @Validated @RequestBody User user) throws UserNotFoundException {
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
