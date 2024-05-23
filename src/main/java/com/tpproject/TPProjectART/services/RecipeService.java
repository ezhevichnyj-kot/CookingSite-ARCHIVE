package com.tpproject.TPProjectART.services;

import ch.qos.logback.core.rolling.helper.*;
import com.tpproject.TPProjectART.entities.*;
import com.tpproject.TPProjectART.repositories.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.authorization.method.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class RecipeService {
    @Autowired
    private IERecipeRepos recipeRepos;

    @Autowired
    private IEConnectorUserRecipe connectorUserRecipe;

    @Autowired IEUserRepos userRepos;

    public ArrayList<ERecipe> getRecipesByCount(Integer startAt, Integer Count) {
        ArrayList<ERecipe> recipes = new ArrayList<ERecipe>();

        ArrayList<ERecipe> all_recipes_arrList = new ArrayList<>();
        Iterable<ERecipe> all_recipes_iterable = recipeRepos.findAll();
        all_recipes_iterable.forEach(all_recipes_arrList::add);

        int k = 0;
        while (k != all_recipes_arrList.size())
        {
            if (all_recipes_arrList.get(k).getChecked() == false)
                all_recipes_arrList.remove(k);
            else
                k++;
        }

        while (all_recipes_arrList.size() > 0)
            if (all_recipes_arrList.get(0).getId() <= startAt)
                all_recipes_arrList.remove(0);
            else
                break;

        while (all_recipes_arrList.size() > Count)
        {
            all_recipes_arrList.remove(all_recipes_arrList.size()-1);
        }

        return all_recipes_arrList;
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
        an_recipe.setName("Рецепт номер " + an_recipe.getId());
        an_recipe.setChecked(false);
        recipeRepos.save(an_recipe);
        return an_recipe;
    }

    public  void saveRecipe(ERecipe an_recipe) {
        recipeRepos.save(an_recipe);
    }

    public ArrayList <ERecipe> SearchByName(Integer count, String nameToFind){

        if (nameToFind == null)
            return getRecipesByCount(0, count);

        Iterable<ERecipe> iterable = recipeRepos.findAll();
        ArrayList <ERecipe> Recipes = new ArrayList<>();
        iterable.forEach(Recipes::add);

        int k = 0;
        while (k != Recipes.size())
        {
            if (Recipes.get(k).getChecked() == false)
                Recipes.remove(k);
            else
                k++;
        }

        ArrayList <ERecipe> Resalts = new ArrayList<>();
        ArrayList <Integer> resalts_similarity = new ArrayList<>();

        String str1 = nameToFind;
        for (ERecipe a : Recipes) {
            char[] name = a.getName().toCharArray();
            char[] search = str1.toCharArray();
            int rating = 0;

            // rating for name

            for (int i = 0; i < str1.length(); i++) {
                for (int j = 0; j < a.getName().length(); j++) {
                    if (name[j] == search[i]) {
                        rating++;
                    }
                }
            }

            if (rating >= (str1.length()*3/4)) {
                Resalts.add(a);
                resalts_similarity.add(rating);
            }
        }

        for (Integer i = resalts_similarity.size()-1; i > 0; i--) {
            for (Integer j = 0; j < i; j++)
            {
                if (resalts_similarity.get(i) < resalts_similarity.get(j))
                {
                    ERecipe temporary_recipe = Resalts.get(i);
                    Resalts.set(i, Resalts.get(j));
                    Resalts.set(j, temporary_recipe);

                    Integer temporary_singularity = resalts_similarity.get(i);
                    resalts_similarity.set(i, resalts_similarity.get(j));
                    resalts_similarity.set(j, temporary_singularity);
                }
            }
        }

        while (Resalts.size() > count)
            Resalts.remove(Resalts.size() - 1);

        return Resalts;
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

    public ArrayList<ERecipe> getRecipesByParams(Integer count, String name, String category, String[] ingredients, String time) {
        ArrayList<ERecipe> recipes_arrList = null;
        if (name != null)
            recipes_arrList = SearchByName(count, name);

        if (recipes_arrList == null)
        {
            recipes_arrList = new ArrayList<>();
            Iterable<ERecipe> iterable = recipeRepos.findAll();
            iterable.forEach(recipes_arrList::add);

            int k = 0;
            while (k != recipes_arrList.size())
            {
                if (recipes_arrList.get(k).getChecked() == false)
                    recipes_arrList.remove(k);
                else
                    k++;
            }
        }

        ArrayList<Integer> raiting = new ArrayList<>();
        for (int i = 0; i < recipes_arrList.size(); i++)
        {
            int one_raiting = 0;

            if (name != null)
                if (recipes_arrList.get(i).getName().contains(name))
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

    public ArrayList<ERecipe> getUncheckedRecipes(Integer count) {
        Iterable<ERecipe> irecipes = recipeRepos.findAll();
        ArrayList<ERecipe> recipes = new ArrayList<>();
        irecipes.forEach(recipes::add);

        int k = 0;
        while (k < recipes.size())
        {
            if (recipes.get(k).getChecked())
                recipes.remove(k);
            else
                k++;
        }

        while (recipes.size() > count)
            recipes.remove(recipes.size()-1);
        return recipes;
    }
}
