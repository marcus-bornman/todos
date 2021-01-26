package com.marcusbornman.spring_todos.uicontrollers;

import com.marcusbornman.spring_todos.entities.User;
import com.marcusbornman.spring_todos.exceptions.UsernameExistsException;
import com.marcusbornman.spring_todos.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
@RequestMapping("/register")
public class RegistrationController {
    private final UserService userService;

    @GetMapping
    public String showRegistrationForm(WebRequest request, Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "register";
    }

    @PostMapping
    public ModelAndView registerUserAccount(@ModelAttribute("user") @Validated User user, HttpServletRequest request, Errors errors) {
        User registered;

        try {
            registered = userService.create(user);
        } catch (UsernameExistsException e) {
            ModelAndView mav = new ModelAndView("register", "user", user);
            mav.addObject("message", "An account for that username already exists.");
            return mav;
        }

        return new ModelAndView("login", "user", registered);
    }
}
