package com.example.ChefServer.repositories;

import com.example.ChefServer.entities.*;
import org.springframework.data.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface IEConnectorUserRecipe extends CrudRepository<EConnectorUserRecipe, Long> {
    public EConnectorUserRecipe[] findAllByUserid(Long userid);
    public EConnectorUserRecipe findByRecipeid(Long recipeid);
}