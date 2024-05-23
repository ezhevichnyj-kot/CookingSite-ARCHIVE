package com.example.ChefServer.repositories;

import com.example.ChefServer.entities.ERecipe;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface IERecipeRepos extends CrudRepository<ERecipe, Long> {
    public ERecipe findById(Integer id);
    public Page<ERecipe> findByVerifiedTrueAndIdGreaterThan(Long startAt, Pageable pageable);
    public List<ERecipe> findAllByVerifiedTrue();
}
