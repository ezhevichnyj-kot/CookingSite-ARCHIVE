package com.tpproject.TPProjectART.controllers;

import com.tpproject.TPProjectART.entities.*;
import com.tpproject.TPProjectART.services.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.core.context.*;
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

    @GetMapping ("/main")
    public String main(Model model) {

        model.addAttribute("isAuthenticated", userService.authenticatedIsNotAnonymous());
        return "main";
    }

    @CrossOrigin
    @GetMapping ("/main/get_recipe")
    @ResponseBody
    public ArrayList<ERecipe> add_recipe_main(@RequestParam(name="count", required = false) Integer count, @RequestParam(name="begin", required = false) Integer startsAt ,Model model) {
        if (count == null) count = 3;
        ArrayList<ERecipe> recipes = recipeService.getRecipesByCount(startsAt, count);
        return recipes;
    }
}
