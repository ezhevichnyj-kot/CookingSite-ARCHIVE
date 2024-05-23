package com.tpproject.TPProjectART.controllers;

import com.tpproject.TPProjectART.entities.*;
import com.tpproject.TPProjectART.services.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.core.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class SearchController {
    @Autowired
    private RecipeService recipeService;

    @Autowired
    private UserService userService;

    @GetMapping("/search")
    public String search(Model model) {
        model.addAttribute("isAuthenticated", userService.authenticatedIsNotAnonymous());
        return "search";
    }

    @CrossOrigin
    @GetMapping ("/search_recipes")
    @ResponseBody
    public ArrayList<ERecipe> add_recipe_main(@RequestParam(name="count", required = false) Integer find_count,
                                              @RequestParam(name="ingredients_arr", required = false) String[] find_ingredients,
                                              @RequestParam(name="find_name", required = false) String find_name,
                                              @RequestParam(name="category", required = false) String find_category,
                                              @RequestParam(name="time", required = false) String find_time,
                                              Model model) {
        ArrayList<ERecipe> recipes = recipeService.getRecipesByParams(find_count, find_name, find_category, find_ingredients, find_time);
        return recipes;
    }

    @CrossOrigin
    @GetMapping ("/profile/get_created_recipes")
    @ResponseBody
    public ArrayList<ERecipe> add_created_recipes(Model model) {
        EUser current = userService.getAuthenticatedUser();
        EConnectorUserRecipe[] connectors = recipeService.getConnectorsByUser(current.getId());
        ArrayList<ERecipe> recipes = new ArrayList<>();
        for (int i = 0; i < connectors.length; i++)
            recipes.add(recipeService.getRecipeById(connectors[i].getRecipeid()));
        return recipes;
    }


}
