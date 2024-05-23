package com.tpproject.TPProjectART.repositories;

import com.tpproject.TPProjectART.entities.*;
import org.springframework.data.repository.*;
import org.springframework.stereotype.*;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface IEUserRepos extends CrudRepository<EUser, Long> {
    public EUser findByUsername(String username);
}
