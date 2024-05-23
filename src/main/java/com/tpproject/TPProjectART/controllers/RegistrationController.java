package com.tpproject.TPProjectART.controllers;

import com.tpproject.TPProjectART.entities.*;
import com.tpproject.TPProjectART.services.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;

    @GetMapping("/registration")
    public String registration(){
        return "registration";
    }

    @PostMapping("/registration")
    public String AddNewUser(EUser user, Model model){
        if (userService.registerNewUser(user)) {
            model.addAttribute("registerMessage","Вы были успешно зарегестрированы!");
            return "redirect:/login";
        }
        else
        {
            model.addAttribute("registerMessage","Пользователь с таким именем уже существует!");
            return "registration";
        }

    }
}
