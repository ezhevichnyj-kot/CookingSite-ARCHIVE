package com.tpproject.TPProjectART.controllers;

import org.springframework.security.web.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;

import javax.naming.*;
import javax.servlet.http.*;

@Controller
public class LoginController {
    @GetMapping("/login")
    public String login(Model model) {
        return "login"; }
}
