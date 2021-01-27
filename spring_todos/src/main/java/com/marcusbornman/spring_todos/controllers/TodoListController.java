package com.marcusbornman.spring_todos.controllers;

import com.marcusbornman.spring_todos.entities.TodoList;
import com.marcusbornman.spring_todos.exceptions.TodoListNotFoundException;
import com.marcusbornman.spring_todos.exceptions.UserNotFoundException;
import com.marcusbornman.spring_todos.services.TodoListService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "Todo Lists")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/{username}/todoLists")
public class TodoListController {
    private final TodoListService todoListService;

    @Operation(summary = "Add a new Todo List")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("isFullyAuthenticated() and #username == principal.username")
    TodoList postTodoList(@PathVariable String username, @Validated @RequestBody TodoList todoList) throws UserNotFoundException {
        return todoListService.create(username, todoList);
    }

    @Operation(
            summary = "Get all todo lists for the authenticated user",
            security = @SecurityRequirement(name = "Basic Authentication")
    )
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("isFullyAuthenticated() and #username == principal.username")
    List<TodoList> getTodoLists(@PathVariable String username) {
        return todoListService.readAll(username);
    }

    @Operation(
            summary = "Get the details of a specific Todo List belonging to the authenticated user",
            security = @SecurityRequirement(name = "Basic Authentication")
    )
    @GetMapping("/{todoListId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("isFullyAuthenticated() and #username == principal.username")
    TodoList getTodoList(@PathVariable String username, @PathVariable Long todoListId) throws TodoListNotFoundException {
        return todoListService.read(username, todoListId);
    }

    @Operation(
            summary = "Update the details of a specific Todo List belonging to the authenticated user",
            security = @SecurityRequirement(name = "Basic Authentication")
    )
    @PutMapping("/{todoListId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("isFullyAuthenticated() and #username == principal.username")
    void putTodoList(@PathVariable String username, @PathVariable Long todoListId, @Validated @RequestBody TodoList todoList) throws TodoListNotFoundException {
        todoListService.update(username, todoListId, todoList);
    }

    @Operation(
            summary = "Update the details of a user with a specific username",
            security = @SecurityRequirement(name = "Basic Authentication")
    )
    @DeleteMapping("/{todoListId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("isFullyAuthenticated() and #username == principal.username")
    void deleteTodoList(@PathVariable String username, @PathVariable Long todoListId) throws TodoListNotFoundException {
        todoListService.delete(username, todoListId);
    }
}
