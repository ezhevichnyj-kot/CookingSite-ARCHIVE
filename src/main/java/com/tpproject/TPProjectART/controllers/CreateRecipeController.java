package com.tpproject.TPProjectART.controllers;

import com.tpproject.TPProjectART.entities.*;
import com.tpproject.TPProjectART.repositories.*;
import com.tpproject.TPProjectART.services.*;
import com.tpproject.TPProjectART.services.ImageService.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.core.style.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.*;
import org.springframework.web.servlet.mvc.support.*;
import org.thymeleaf.context.*;

import javax.persistence.*;
import javax.servlet.*;
import java.io.*;

@Controller
public class CreateRecipeController {

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private UserService userService;

    @Autowired
    private IEUserRepos userRepos;

    @Value("${upload.path}")
    private String upload_path;

    @GetMapping(path = "/create_recipe")
    public String create_recipe(RedirectAttributes redirectAttributes) {

        EUser user = userService.getAuthenticatedUser();
        if (user == null) {
            return "redirect:/login";
        }

        ERecipe an_recipe = recipeService.createRecipe();
        EConnectorUserRecipe connectorUserRecipe = new EConnectorUserRecipe();
        connectorUserRecipe.setRecipe_id(an_recipe.getId());
        connectorUserRecipe.setUser_id(user.getId().longValue());
        recipeService.saveConnector(connectorUserRecipe);

        redirectAttributes.addAttribute("id", an_recipe.getId());
        return "redirect:/create_recipe/{id}";
    }

    @GetMapping(path = "/create_recipe/{id}")
    public String create_recipe_get(@PathVariable(name = "id") Long id, Model model) {

        if (recipeService.getRecipeById(id) == null)
            return "redirect:/error?code=404";

        EUser user = userService.getAuthenticatedUser();
        if (user == null) {
            return "redirect:/login";
        }

        ERecipe an_recipe =  recipeService.getRecipeById(id);
        Long id_copy = id.longValue();
        EConnectorUserRecipe connectorUserRecipe = recipeService.getConnectorByRecipe(id_copy);

        EUser recipe_author = userRepos.findById(connectorUserRecipe.getUserid()).get();
        if (recipe_author != user)
            return "redirect:/error?code=406";

        model.addAttribute("isAuthenticated", true);
        model.addAttribute("showRecipe", an_recipe);
        return "create_recipe";
    }

    @PostMapping(path = "/change-recipe-image/{id}")
    public String changeImage(@PathVariable(name="id") Long id,
                              @RequestParam(name="recipe_image", required = false) MultipartFile recipe_image, RedirectAttributes redirectAttributes) throws IOException {

        EUser user = userService.getAuthenticatedUser();
        if (user == null) {
            return "redirect:/error?code=406";
        }

        ERecipe an_recipe =  recipeService.getRecipeById(id);
        Long id_copy = id.longValue();
        EConnectorUserRecipe connectorUserRecipe = recipeService.getConnectorByRecipe(id_copy);

        EUser recipe_author = userRepos.findById(connectorUserRecipe.getUserid()).get();
        if (recipe_author != user)
            return "redirect:/error?&code=406";

        if (recipe_image != null)
        {
            String fileName = Long.toString(id) + ".jpg";

            File uploadDir = new File(upload_path);
            if (!uploadDir.exists())
                uploadDir.mkdir();

            FileUploadUtil.saveFile(upload_path, fileName, recipe_image);

            an_recipe.setImage_path(upload_path + "/" + fileName);
        }
        else
        {
            an_recipe.setImage_path(null);
        }

        recipeService.saveRecipe(an_recipe); // сохранение перезаписанного рецепта

        redirectAttributes.addFlashAttribute("id", id);
        return "redirect:/create_recipe/{id}";
    }

    @CrossOrigin
    @GetMapping(path = "/create_recipe_update")
    @ResponseBody
    public String create_recipe_post(@RequestParam(name="id", required = true) Long id,
                                     @RequestParam(name="description", required = false) String description,
                                     @RequestParam(name="recipe_name", required = false) String recipe_name,
                                     @RequestParam(name="cook_steps", required = false) String cook_steps,
                                     @RequestParam(name="cook_time", required = false) String cook_time,
                                     @RequestParam(name="category", required = false) String category) {

        EUser user = userService.getAuthenticatedUser();
        if (user == null) {
            return "false";
        }

        ERecipe an_recipe =  recipeService.getRecipeById(id);
        Long id_copy = id.longValue();
        EConnectorUserRecipe connectorUserRecipe = recipeService.getConnectorByRecipe(id_copy);

        EUser recipe_author = userRepos.findById(connectorUserRecipe.getUserid()).get();
        if (recipe_author != user)
            return "false";

        if (description != null)
            an_recipe.setDescription(description);
        if (recipe_name != null)
            an_recipe.setName(recipe_name);
        if (cook_time != null)
            an_recipe.setCook_time(cook_time);
        if (category != null)
            an_recipe.setCategory(category);
        if (cook_steps != null)
            an_recipe.setCook_steps(cook_steps);

        recipeService.saveRecipe(an_recipe); // сохранение перезаписанного рецепта

        return "true";
    }


}
