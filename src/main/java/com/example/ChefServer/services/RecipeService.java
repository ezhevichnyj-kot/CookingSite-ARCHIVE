package com.example.ChefServer.services;

import com.example.ChefServer.entities.ERecipe;
import com.example.ChefServer.entities.EConnectorUserRecipe;
import com.example.ChefServer.repositories.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.*;
import org.springframework.data.domain.Pageable;

import java.util.*;

@Service
public class RecipeService {
    @Autowired
    private IERecipeRepos recipeRepos;

    @Autowired
    private IEConnectorUserRecipe connectorUserRecipe;

    @Autowired IEUserRepos userRepos;

    public List<ERecipe> getRecipesByCount(Long startAt, Integer count) {
        Pageable pageable = PageRequest.of(0, count);
        return recipeRepos.findByVerifiedTrueAndIdGreaterThan(startAt, pageable).getContent();
    }

    public ERecipe getRecipeById(Long recipe_id) {

        if (recipeRepos.findById(recipe_id).isEmpty() == false) {
            ERecipe an_recipe = recipeRepos.findById(recipe_id).get();
            return  an_recipe;
        }
        return null;
    }

    public ERecipe createRecipe() {
        ERecipe an_recipe = new ERecipe();

        recipeRepos.save(an_recipe);
        an_recipe.setTitle("Рецепт номер " + an_recipe.getId());
        an_recipe.setVerified(false);
        recipeRepos.save(an_recipe);
        return an_recipe;
    }

    public  void saveRecipe(ERecipe an_recipe) {
        recipeRepos.save(an_recipe);
    }

    public List<ERecipe> searchByName(Integer count, String nameToFind) {
        if (nameToFind == null) {
            return getRecipesByCount(0l, count);
        }
    
        List<ERecipe> recipes = recipeRepos.findAllByVerifiedTrue();
    
        List<ERecipe> matchingRecipes = new ArrayList<>();
        for (ERecipe recipe : recipes) {
            if (isSimilar(recipe.getTitle(), nameToFind)) {
                matchingRecipes.add(recipe);
            }
        }

        if (matchingRecipes.size() > count) {
            matchingRecipes = matchingRecipes.subList(0, count);
        }
    
        return matchingRecipes;
    }
    
    private boolean isSimilar(String recipeName, String nameToFind) {
        // Простое сравнение символов
        int similarityThreshold = nameToFind.length() * 3 / 4;
        int matchingChars = 0;
        for (char c : recipeName.toCharArray()) {
            if (nameToFind.contains(String.valueOf(c))) {
                matchingChars++;
            }
        }
        return matchingChars >= similarityThreshold;
    }

    public EConnectorUserRecipe[] getConnectorsByUser(Long id) {
        return connectorUserRecipe.findAllByUserid(id);
    }

    public EConnectorUserRecipe getConnectorByRecipe(Long id) {
        return connectorUserRecipe.findByRecipeid(id);
    }

    public void saveConnector(EConnectorUserRecipe connector) {
        connectorUserRecipe.save(connector);
    }

    public void deleteRecipe(Long id) {
        recipeRepos.deleteById(id);
        EConnectorUserRecipe connector = getConnectorByRecipe(id);
        connectorUserRecipe.deleteById(connector.getId());
    }

    public List<ERecipe> getRecipesByParams(Integer count, String name, String category, String[] ingredients, String time) {
        List<ERecipe> recipes_arrList = null;
        if (name != null)
            recipes_arrList = searchByName(count, name);

        if (recipes_arrList == null)
        {
            recipes_arrList = new ArrayList<>();
            Iterable<ERecipe> iterable = recipeRepos.findAll();
            iterable.forEach(recipes_arrList::add);

            int k = 0;
            while (k != recipes_arrList.size())
            {
                if (recipes_arrList.get(k).getVerified() == false)
                    recipes_arrList.remove(k);
                else
                    k++;
            }
        }

        List<Integer> raiting = new ArrayList<>();
        for (int i = 0; i < recipes_arrList.size(); i++)
        {
            int one_raiting = 0;

            if (name != null)
                if (recipes_arrList.get(i).getTitle().contains(name))
                    one_raiting += 6;

            if (category != null && recipes_arrList.get(i).getCategory() != null)
                if (category.equals(recipes_arrList.get(i).getCategory()))
                {
                    one_raiting += 5;
                }

            if (time != null && recipes_arrList.get(i).getCook_time() != null) {
                String[] timeablestr = time.split(":");
                int[] timeable = new int[2];

                try {
                    timeable[0] = Integer.parseInt(timeablestr[0]);
                    timeable[1] = Integer.parseInt(timeablestr[1]);
                }
                catch (NumberFormatException e) {
                    timeable[0] = 0;
                    timeable[1] = 0;
                }
                int recipe_time = timeable[0]*60 + timeable[1];

                timeablestr = recipes_arrList.get(i).getCook_time().split(":");
                timeable = new int[2];

                try {
                    timeable[0] = Integer.parseInt(timeablestr[0]);
                    timeable[1] = Integer.parseInt(timeablestr[1]);
                }
                catch (NumberFormatException e) {
                    timeable[0] = 0;
                    timeable[1] = 0;
                }
                int summary_time = timeable[0]*60 + timeable[1];

                if (recipe_time <= summary_time)
                {
                    one_raiting += 5;
                }
            }


            if (ingredients != null && recipes_arrList.get(i).getIngredients() != null)
                for (int j = 0; j < ingredients.length; j++)
                {
                    for (int l = 0; l < recipes_arrList.get(i).getIngredients().size(); l++)
                    {
                        if (recipes_arrList.get(i).getIngredients().get(l).equals(ingredients[j]))
                        {
                            one_raiting += 1;
                        }
                    }
                }

            raiting.add(one_raiting);
        }

        for (Integer i = raiting.size()-1; i > 0; i--) {
            for (Integer j = 0; j < i; j++)
            {
                if (raiting.get(j) < raiting.get(j+1))
                {
                    ERecipe temporary_recipe = recipes_arrList.get(j);
                    recipes_arrList.set(j, recipes_arrList.get(j+1));
                    recipes_arrList.set(j+1, temporary_recipe);

                    Integer temporary_singularity = raiting.get(j);
                    raiting.set(j, raiting.get(j+1));
                    raiting.set(j+1, temporary_singularity);
                }
            }
        }

        while (recipes_arrList.size() > count)
            recipes_arrList.remove(recipes_arrList.size()-1);

        return recipes_arrList;
    }

    public List<ERecipe> getUncheckedRecipes(Integer count) {
        Iterable<ERecipe> irecipes = recipeRepos.findAll();
        List<ERecipe> recipes = new ArrayList<>();
        irecipes.forEach(recipes::add);

        int k = 0;
        while (k < recipes.size())
        {
            if (recipes.get(k).getVerified())
                recipes.remove(k);
            else
                k++;
        }

        while (recipes.size() > count)
            recipes.remove(recipes.size()-1);
        return recipes;
    }
}
