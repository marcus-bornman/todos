package com.marcusbornman.spring_todos.controllers;

import com.marcusbornman.spring_todos.entities.Todo;
import com.marcusbornman.spring_todos.entities.TodoList;
import com.marcusbornman.spring_todos.entities.User;
import com.marcusbornman.spring_todos.exceptions.TodoListNotFoundException;
import com.marcusbornman.spring_todos.exceptions.TodoNotFoundException;
import com.marcusbornman.spring_todos.exceptions.UserNotFoundException;
import com.marcusbornman.spring_todos.exceptions.UsernameExistsException;
import com.marcusbornman.spring_todos.services.TodoListService;
import com.marcusbornman.spring_todos.services.TodoService;
import com.marcusbornman.spring_todos.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class UiController {
    private final UserService userService;
    private final TodoListService todoListService;
    private final TodoService todoService;

    @GetMapping("/")
    public String getIndex(Model model) {
        User user = authenticatedUser();
        if (user == null) return "index";

        model.addAttribute("newTodoList", new TodoList());
        model.addAttribute("newTodo", new Todo());
        model.addAttribute("todoLists", todoListService.readAll(user.getUsername()));

        return "home";
    }

    @GetMapping("/register")
    public String getRegister(Model model) {
        if (authenticatedUser() != null) return "redirect:/";

        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String postRegister(@ModelAttribute("user") @Validated User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) return "register";

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
    public String getLogin() {
        if (authenticatedUser() != null) return "redirect:/";

        return "login";
    }

    @PostMapping("/todoLists")
    @PreAuthorize("isFullyAuthenticated()")
    public String postTodoList(@ModelAttribute("newTodoList") @Validated TodoList todoList, BindingResult bindingResult) throws UserNotFoundException {
        if (bindingResult.hasErrors()) return "home";

        String username = Objects.requireNonNull(authenticatedUser()).getUsername();
        todoListService.create(username, todoList);

        return "redirect:/";
    }

    @GetMapping("/todoLists/{todoListId}")
    @PreAuthorize("isFullyAuthenticated()")
    public String deleteTodoList(@PathVariable Long todoListId) throws TodoListNotFoundException {
        String username = Objects.requireNonNull(authenticatedUser()).getUsername();
        todoListService.delete(username, todoListId);

        return "redirect:/";
    }

    @PostMapping("/todoLists/{todoListId}/todos")
    @PreAuthorize("isFullyAuthenticated()")
    public String postTodo(@PathVariable Long todoListId, @ModelAttribute("newTodo") @Validated Todo todo, BindingResult bindingResult) throws TodoListNotFoundException {
        if (bindingResult.hasErrors()) return "home";

        String username = Objects.requireNonNull(authenticatedUser()).getUsername();
        todoService.create(username, todoListId, todo);

        return "redirect:/";
    }

    @GetMapping("/todoLists/{todoListId}/todos/{todoId}")
    @PreAuthorize("isFullyAuthenticated()")
    public String deleteTodo(@PathVariable Long todoListId, @PathVariable Long todoId) throws TodoNotFoundException {
        String username = Objects.requireNonNull(authenticatedUser()).getUsername();
        todoService.delete(username, todoListId, todoId);

        return "redirect:/";
    }

    private User authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || AnonymousAuthenticationToken.class.
                isAssignableFrom(authentication.getClass())) {
            return null;
        }
        return (User) authentication.getPrincipal();
    }
}
