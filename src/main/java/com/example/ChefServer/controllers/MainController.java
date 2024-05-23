package com.example.ChefServer.controllers;

import com.example.ChefServer.entities.*;
import com.example.ChefServer.services.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class MainController {

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private UserService userService;

    @GetMapping ("/")
    public String main(Model model) {

        model.addAttribute("isAuthenticated", userService.authenticatedIsNotAnonymous());
        return "main";
    }

    @GetMapping ("/main/get_recipe")
    @ResponseBody
    public List<ERecipe> add_recipe_main(
            @RequestParam(name="count", required = false, defaultValue="3") Integer count,
            @RequestParam(name="begin", required = false) Integer startsAt ,
            Model model) {

        List<ERecipe> recipes = recipeService.getRecipesByCount((long)startsAt, count);
        return recipes;
    }
}
