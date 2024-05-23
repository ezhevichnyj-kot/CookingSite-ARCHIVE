package com.example.ChefServer.controllers;

import com.example.ChefServer.entities.*;
import com.example.ChefServer.repositories.*;
import com.example.ChefServer.services.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/recipe")
public class RecipeController {
    @Autowired
    private IEIngredientRepos ingredientRepos;

    @Autowired
    private  IEUserRepos userRepos;

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private UserService userService;

    @GetMapping ("/user")
    @ResponseBody
    public ArrayList<ERecipe> add_created_recipes(Model model) {
        EUser current = userService.getAuthenticatedUser();
        EConnectorUserRecipe[] connectors = recipeService.getConnectorsByUser(current.getId());
        ArrayList<ERecipe> recipes = new ArrayList<>();
        for (int i = 0; i < connectors.length; i++)
            recipes.add(recipeService.getRecipeById(connectors[i].getId_recipe()));
        return recipes;
    }

    @GetMapping("/{id}")
    public String recipe(@PathVariable(name = "id") Long id, Model model) {

        if (recipeService.getRecipeById(id) == null)
            return "redirect:/error?error=Указанного рецепта не существует!&code=404";

        ERecipe an_recipe =  recipeService.getRecipeById(id);

        model.addAttribute("isAuthenticated", userService.authenticatedIsNotAnonymous());
        model.addAttribute("showRecipe", an_recipe);
        return "recipe";
    }

    @GetMapping("/get_ingredients")
    @ResponseBody
    public ArrayList<String> getIngredients(
            @RequestParam(name="id", required = false) Long id) 
    {
        if (id == null)
            return null;

        ArrayList<String> ingreds = recipeService.getRecipeById(id).getIngredients();
        
        if (ingreds == null)
            ingreds = new ArrayList<>();

        return ingreds;
    }

    @GetMapping("/get_all_ingredients")
    @ResponseBody
    public ArrayList<EIngredient> getAllIngredients() {
        ArrayList<EIngredient> ingredients = new ArrayList<>();
        ingredientRepos.findAll().forEach(ingredients::add);

        return ingredients;
    }

    @DeleteMapping("/delete_recipe")
    @ResponseBody
    public String delete_recipe(@RequestParam(name="id") Long id, Model model)
    {
        EUser current = userService.getAuthenticatedUser();
        if (userService.authenticatedIsNotAnonymous()) {
            EConnectorUserRecipe connector = recipeService.getConnectorByRecipe(id);
            if (current.getId().equals(connector.getId_user())) {
                recipeService.deleteRecipe(id);
                return "Успешно удалено" + current.getId();
            } else
                return "Неверный пароль";
        }
        else
            return "Пользователь не авторизован";
    }

    @PostMapping("/add_ingredient")
    @ResponseBody
    public String addIngredient(@RequestParam(name="name") String name, @RequestParam(name="id") Long id) {
        EUser user = userService.getAuthenticatedUser();
        if (user == null) {
            return "Пользователь не авторизован!";
        }

        ERecipe an_recipe =  recipeService.getRecipeById(id);
        Long id_copy = id.longValue();
        EConnectorUserRecipe connectorUserRecipe = recipeService.getConnectorByRecipe(id_copy);

        EUser recipe_author = userRepos.findById(connectorUserRecipe.getId_user()).get();
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
