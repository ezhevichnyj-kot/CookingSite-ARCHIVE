package com.tpproject.TPProjectART.repositories;

import com.tpproject.TPProjectART.entities.*;
import org.springframework.data.repository.*;

public interface IERecipeRepos extends CrudRepository<ERecipe, Long> {
    public ERecipe findById(Integer id);
}
