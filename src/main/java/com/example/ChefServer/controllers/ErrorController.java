package com.example.ChefServer.controllers;

import com.example.ChefServer.services.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;

@Controller
public class ErrorController {
    @Autowired
    private UserService userService;

    @GetMapping(path="/user/login-error")
    public String loginError(Model model) {
        return "redirect:/user/login?error=true";
    }

    @GetMapping(path="/error")
    public String errorPage(@RequestParam(name="message", required = false) String errorMessage,
                            @RequestParam(name="code", required = false) String errorCode,
                            Model model) {
        if (errorMessage != null)
            model.addAttribute("error_message",errorMessage);
        if (errorCode != null)
            model.addAttribute("error_code",errorCode);

        model.addAttribute("isAuthenticated", userService.authenticatedIsNotAnonymous());
        return "error";
    }
}
