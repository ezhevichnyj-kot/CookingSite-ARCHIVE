package com.example.ChefServer.repositories;

import com.example.ChefServer.entities.*;
import org.springframework.data.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface IEUserRepos extends CrudRepository<EUser, Long> {
    public EUser findByUsername(String username);
}
