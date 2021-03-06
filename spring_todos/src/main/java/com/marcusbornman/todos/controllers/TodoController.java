package com.marcusbornman.todos.controllers;

import com.marcusbornman.todos.entities.Todo;
import com.marcusbornman.todos.exceptions.TodoListNotFoundException;
import com.marcusbornman.todos.exceptions.TodoNotFoundException;
import com.marcusbornman.todos.services.TodoService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;
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

@Api(tags = "Todos")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/{username}/todoLists/{todoListId}/todos")
public class TodoController {

  private final TodoService todoService;

  @Operation(summary = "Add a new Todo")
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @PreAuthorize("isFullyAuthenticated() and #username == principal.username")
  Todo postTodoList(@PathVariable String username, @PathVariable Long todoListId,
      @Validated @RequestBody Todo todo) throws TodoListNotFoundException {
    return todoService.create(username, todoListId, todo);
  }

  @Operation(
      summary = "Get all Todos for the specified todo list",
      security = @SecurityRequirement(name = "Basic Authentication")
  )
  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isFullyAuthenticated() and #username == principal.username")
  List<Todo> getTodoLists(@PathVariable String username, @PathVariable Long todoListId) {
    return todoService.readAll(username, todoListId);
  }

  @Operation(
      summary = "Get the details of a specific Todo belonging to the authenticated user",
      security = @SecurityRequirement(name = "Basic Authentication")
  )
  @GetMapping("/{todoId}")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("isFullyAuthenticated() and #username == principal.username")
  Todo getTodoList(@PathVariable String username, @PathVariable Long todoListId,
      @PathVariable Long todoId) throws TodoNotFoundException {
    return todoService.read(username, todoListId, todoId);
  }

  @Operation(
      summary = "Update the details of a specific Todo belonging to the authenticated user",
      security = @SecurityRequirement(name = "Basic Authentication")
  )
  @PutMapping("/{todoId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize("isFullyAuthenticated() and #username == principal.username")
  void putTodoList(@PathVariable String username, @PathVariable Long todoListId,
      @PathVariable Long todoId, @Validated @RequestBody Todo todo) throws TodoNotFoundException {
    todoService.update(username, todoListId, todoId, todo);
  }

  @Operation(
      summary = "Update the details of a specific Todo",
      security = @SecurityRequirement(name = "Basic Authentication")
  )
  @DeleteMapping("/{todoId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize("isFullyAuthenticated() and #username == principal.username")
  void deleteTodoList(@PathVariable String username, @PathVariable Long todoListId,
      @PathVariable Long todoId) throws TodoNotFoundException {
    todoService.delete(username, todoListId, todoId);
  }
}
