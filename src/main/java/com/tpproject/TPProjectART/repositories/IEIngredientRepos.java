package com.tpproject.TPProjectART.repositories;

import com.tpproject.TPProjectART.entities.*;
import org.springframework.data.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface IEIngredientRepos extends CrudRepository<EIngredient, Long> {
    public EIngredient findByName(String name);
}
