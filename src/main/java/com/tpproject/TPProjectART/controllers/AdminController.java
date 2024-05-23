package com.tpproject.TPProjectART.controllers;

import com.tpproject.TPProjectART.entities.*;
import com.tpproject.TPProjectART.repositories.*;
import com.tpproject.TPProjectART.services.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.*;

import javax.persistence.*;
import java.util.*;

@Controller
public class AdminController {
    @Autowired
    private IngredientService ingredientService;

    @Autowired
    private UserService userService;

    @Autowired
    private RecipeService recipeService;

    @GetMapping(path = "/admin")
    public String create_recipe(Model model) {

        if (userService.authenticatedIsNotAnonymous())
        {
            Set<EMRole> roles = userService.getAuthenticatedUser().getRoles();
            if (roles.contains(EMRole.ADMIN))
                return "admin";
        }
        return "redirect:/error?code=406";
    }

    @CrossOrigin
    @GetMapping(path = "/get_unchecked_recipes")
    @ResponseBody
    public ArrayList<ERecipe> getUnchecked(@RequestParam(name="count", required = false) Integer count) {
        if (count == null)
            count = 12;
        ArrayList<ERecipe> recipes = recipeService.getUncheckedRecipes(count);
        return  recipes;
    }

    @PostMapping(path = "/admin/reloadIngredients")
    public String reloadIngredients(Model model) {
        if (userService.authenticatedIsNotAnonymous())
        {
            Set<EMRole> roles = userService.getAuthenticatedUser().getRoles();
            if (roles.contains(EMRole.ADMIN)) {
                ingredientService.fromFile_pushAll();
                return "redirect:/admin";
            }
        }
        return "redirect:/error?code=406";
    }

    @CrossOrigin
    @GetMapping(path="/controll_recipe")
    @ResponseBody
    public boolean controllRecipe(@RequestParam(name="mode", required = false) Boolean mode,
                                 @RequestParam(name="id", required = false) Long id) {
        if (mode == null || id == null)
            return false;

        if (userService.authenticatedIsNotAnonymous())
        {
            Set<EMRole> roles = userService.getAuthenticatedUser().getRoles();
            if (roles.contains(EMRole.ADMIN)) {
                if (mode)
                {
                    ERecipe recipe = recipeService.getRecipeById(id);
                    recipe.setChecked(true);
                    recipeService.saveRecipe(recipe);
                    return true;
                }
                else
                {
                    recipeService.deleteRecipe(id);
                    return false;
                }
            }
        }
        return false;
    }
}
