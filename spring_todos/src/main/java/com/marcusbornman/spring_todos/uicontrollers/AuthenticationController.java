package com.marcusbornman.spring_todos.uicontrollers;

import com.marcusbornman.spring_todos.entities.User;
import com.marcusbornman.spring_todos.exceptions.UsernameExistsException;
import com.marcusbornman.spring_todos.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class AuthenticationController {
    private final UserService userService;

    @GetMapping
    public String getHome(Model model) {
        model.addAttribute("user", new User());
        return "home";
    }

    @GetMapping("register")
    public String getRegister(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("register")
    public String postRegister(@ModelAttribute("user") @Validated User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) return "register";

        try {
            userService.create(user);
        } catch (UsernameExistsException e) {
            model.addAttribute("user", new User());
            model.addAttribute("registrationError", "User name already exists.");
            return "register";
        }

        return "login";
    }

    @GetMapping("login")
    public String getLogin() {
        return "login";
    }
}
