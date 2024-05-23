package com.example.ChefServer.controllers;

import com.example.ChefServer.entities.*;
import com.example.ChefServer.services.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/search")
public class SearchController {
    
    @Autowired
    private RecipeService recipeService;

    @GetMapping("/")
    public String search(Model model) {
        return "search";
    }

    @GetMapping ("/recipes")
    @ResponseBody
    public List<ERecipe> add_recipe_main(
            @RequestParam(name="count", required = false) Integer find_count,
            @RequestParam(name="ingredients_arr", required = false) String[] find_ingredients,
            @RequestParam(name="find_name", required = false) String find_name,
            @RequestParam(name="category", required = false) String find_category,
            @RequestParam(name="time", required = false) String find_time,
            Model model) 
    {
        List<ERecipe> recipes = recipeService.getRecipesByParams(find_count, find_name, find_category, find_ingredients, find_time);
        return recipes;
    }
}
