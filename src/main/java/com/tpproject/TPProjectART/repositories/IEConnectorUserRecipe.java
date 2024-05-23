package com.tpproject.TPProjectART.repositories;

import com.tpproject.TPProjectART.entities.*;
import org.springframework.data.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface IEConnectorUserRecipe extends CrudRepository<EConnectorUserRecipe, Long> {
    public EConnectorUserRecipe[] findAllByUserid(Long userid);
    public EConnectorUserRecipe findByRecipeid(Long recipeid);
}