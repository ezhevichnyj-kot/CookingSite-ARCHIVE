package com.example.ChefServer.repositories;

import com.example.ChefServer.entities.*;
import org.springframework.data.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface IEIngredientRepos extends CrudRepository<EIngredient, Long> {
    public EIngredient findByName(String name);
}
