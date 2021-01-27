package com.marcusbornman.todos.controllers;

import com.marcusbornman.todos.entities.Todo;
import com.marcusbornman.todos.entities.TodoList;
import com.marcusbornman.todos.entities.User;
import com.marcusbornman.todos.exceptions.TodoListNotFoundException;
import com.marcusbornman.todos.exceptions.TodoNotFoundException;
import com.marcusbornman.todos.exceptions.UserNotFoundException;
import com.marcusbornman.todos.exceptions.UsernameExistsException;
import com.marcusbornman.todos.services.TodoListService;
import com.marcusbornman.todos.services.TodoService;
import com.marcusbornman.todos.services.UserService;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UiController {

  private final UserService userService;
  private final TodoListService todoListService;
  private final TodoService todoService;

  @GetMapping("/")
  String getIndex(Model model) {
    User user = authenticatedUser();
    if (user == null) {
      return "index";
    }

    model.addAttribute("newTodoList", new TodoList());
    model.addAttribute("newTodo", new Todo());
    model.addAttribute("todoLists", todoListService.readAll(user.getUsername()));

    return "home";
  }

  @GetMapping("/api")
  String getApi() {
    return "redirect:/swagger-ui/";
  }

  @GetMapping("/register")
  String getRegister(Model model) {
    model.addAttribute("user", new User());
    return "register";
  }

  @PostMapping("/register")
  String postRegister(@ModelAttribute("user") @Validated User user,
      BindingResult bindingResult, Model model) {
    if (bindingResult.hasErrors()) {
      return "register";
    }

    try {
      userService.create(user);
    } catch (UsernameExistsException e) {
      model.addAttribute("user", new User());
      model.addAttribute("registrationError", "User name already exists.");
      return "register";
    }

    return "redirect:/login";
  }

  @GetMapping("/login")
  String getLogin() {
    return "login";
  }

  @PostMapping("/todoLists")
  @PreAuthorize("isFullyAuthenticated()")
  String postTodoList(@ModelAttribute("newTodoList") @Validated TodoList todoList,
      BindingResult bindingResult) throws UserNotFoundException {
    if (bindingResult.hasErrors()) {
      return "home";
    }

    String username = Objects.requireNonNull(authenticatedUser()).getUsername();
    todoListService.create(username, todoList);

    return "redirect:/";
  }

  @GetMapping("/todoLists/{todoListId}")
  @PreAuthorize("isFullyAuthenticated()")
  String deleteTodoList(@PathVariable Long todoListId) throws TodoListNotFoundException {
    String username = Objects.requireNonNull(authenticatedUser()).getUsername();
    todoListService.delete(username, todoListId);

    return "redirect:/";
  }

  @PostMapping("/todoLists/{todoListId}/todos")
  @PreAuthorize("isFullyAuthenticated()")
  String postTodo(@PathVariable Long todoListId,
      @ModelAttribute("newTodo") @Validated Todo todo, BindingResult bindingResult)
      throws TodoListNotFoundException {
    if (bindingResult.hasErrors()) {
      return "home";
    }

    String username = Objects.requireNonNull(authenticatedUser()).getUsername();
    todoService.create(username, todoListId, todo);

    return "redirect:/";
  }

  @GetMapping("/todoLists/{todoListId}/todos/{todoId}")
  @PreAuthorize("isFullyAuthenticated()")
  String deleteTodo(@PathVariable Long todoListId, @PathVariable Long todoId)
      throws TodoNotFoundException {
    String username = Objects.requireNonNull(authenticatedUser()).getUsername();
    todoService.delete(username, todoListId, todoId);

    return "redirect:/";
  }

  private User authenticatedUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null
        || AnonymousAuthenticationToken.class.isAssignableFrom(authentication.getClass())) {
      return null;
    }
    return (User) authentication.getPrincipal();
  }
}
