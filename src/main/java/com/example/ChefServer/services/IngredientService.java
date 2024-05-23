package com.example.ChefServer.services;

import com.example.ChefServer.entities.*;
import com.example.ChefServer.repositories.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.io.*;
import java.util.*;

@Service
public class IngredientService {
    
    @Autowired
    private IEIngredientRepos ingredientRepos;

    // Предполагалось что ингредиенты буду браться с txt файла при запуске сервера
    public void fromFile_pushAll() {

        ingredientRepos.deleteAll();

        ArrayList<String> ingreeds = new ArrayList<String>();
        try (BufferedReader reader = new BufferedReader(new FileReader("C:/Users/angel/Desktop/ingredients.txt"))) {
            String one = "";
            one=reader.readLine();
            while(one != null){
                ingreeds.add(one);
                one=reader.readLine();
            }
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }

        Collections.sort(ingreeds);

        for (Integer i = 0; i < ingreeds.size(); i++) {
            EIngredient ing = new EIngredient(ingreeds.get(i));
            ingredientRepos.save(ing);
        }
    }
}
