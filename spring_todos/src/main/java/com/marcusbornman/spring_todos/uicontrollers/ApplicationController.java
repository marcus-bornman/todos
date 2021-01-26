package com.marcusbornman.spring_todos.uicontrollers;

import com.marcusbornman.spring_todos.entities.User;
import com.marcusbornman.spring_todos.exceptions.UsernameExistsException;
import com.marcusbornman.spring_todos.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class ApplicationController {
    private final UserService userService;

    @GetMapping("/")
    public String getIndex() {
        return "index";
    }

    @GetMapping("/register")
    public String getRegister(Model model) {
        if (isAuthenticated()) return "redirect:/";

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
        if (isAuthenticated()) return "redirect:/";

        return "login";
    }

    private boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || AnonymousAuthenticationToken.class.
                isAssignableFrom(authentication.getClass())) {
            return false;
        }
        return authentication.isAuthenticated();
    }
}
