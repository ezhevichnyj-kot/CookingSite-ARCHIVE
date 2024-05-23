package com.tpproject.TPProjectART.controllers;

import com.tpproject.TPProjectART.entities.*;
import com.tpproject.TPProjectART.repositories.*;
import com.tpproject.TPProjectART.services.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class RecipeController {
    @Autowired
    private IEIngredientRepos ingredientRepos;

    @Autowired
    private  IEUserRepos userRepos;

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private UserService userService;

    @GetMapping("/recipe/{id}")
    public String recipe(@PathVariable(name = "id") Long id, Model model) {

        if (recipeService.getRecipeById(id) == null)
            return "redirect:/error?error=Указанного рецепта не существует!&code=404";

        ERecipe an_recipe =  recipeService.getRecipeById(id);

        model.addAttribute("isAuthenticated", userService.authenticatedIsNotAnonymous());
        model.addAttribute("showRecipe", an_recipe);
        return "recipe";
    }

    @CrossOrigin
    @GetMapping("/get_ingredients")
    @ResponseBody
    public ArrayList<String> getIngredients(@RequestParam(name="id", required = false) Long id) {
        if (id == null)
            return null;

        ArrayList<String> ingreds = recipeService.getRecipeById(id).getIngredients();
        if (ingreds == null)
            ingreds = new ArrayList<>();

        return ingreds;
    }

    @CrossOrigin
    @GetMapping("/get_all_ingredients")
    @ResponseBody
    public ArrayList<EIngredient> getAllIngredients() {
        ArrayList<EIngredient> ingredients = new ArrayList<>();
        ingredientRepos.findAll().forEach(ingredients::add);

        return ingredients;
    }

    @CrossOrigin
    @GetMapping("/delete_recipe")
    @ResponseBody
    public String delete_recipe(@RequestParam(name="id") Long id, Model model)
    {
        EUser current = userService.getAuthenticatedUser();
        if (userService.authenticatedIsNotAnonymous()) {
            EConnectorUserRecipe connector = recipeService.getConnectorByRecipe(id);
            if (current.getId().equals(connector.getUserid())) {
                recipeService.deleteRecipe(id);
                return "Успешно удалено" + current.getId();
            } else
                return "Неверный пароль";
        }
        else
            return "Пользователь не авторизован";
    }

    @CrossOrigin
    @GetMapping("/add_ingredient")
    @ResponseBody
    public String addIngredient(@RequestParam(name="name") String name, @RequestParam(name="id") Long id) {
        EUser user = userService.getAuthenticatedUser();
        if (user == null) {
            return "Пользователь не авторизован!";
        }

        ERecipe an_recipe =  recipeService.getRecipeById(id);
        Long id_copy = id.longValue();
        EConnectorUserRecipe connectorUserRecipe = recipeService.getConnectorByRecipe(id_copy);

        EUser recipe_author = userRepos.findById(connectorUserRecipe.getUserid()).get();
        if (recipe_author != user)
            return "Пользователь не является автором рецепта!";

        if (name == null || name.equals(""))
            return "Задан пустой ингредиент";

        ArrayList<String> ingr = an_recipe.getIngredients();
        if (ingr == null)
            ingr = new ArrayList<>();
        ingr.add(name);
        an_recipe.setIngredients(ingr);

        recipeService.saveRecipe(an_recipe);
        return "Ингредиент УСПЕШНО добавлен";
    }
}
