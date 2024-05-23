package com.tpproject.TPProjectART.services;

import com.tpproject.TPProjectART.entities.*;
import com.tpproject.TPProjectART.repositories.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.*;
import org.springframework.security.core.context.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class UserService {

    @Autowired
    private IEUserRepos userRepos;

    public EUser getAuthenticatedUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth instanceof AnonymousAuthenticationToken)
            return null;

        EUser user = userRepos.findByUsername(auth.getName());
        return user;
    }

    public boolean registerNewUser(EUser user){
        EUser userbyUsername = userRepos.findByUsername(user.getUsername());
        if (userbyUsername != null)
        {
            return false;
        }
        user.setActive(true);
        user.setRoles(Collections.singleton(EMRole.USER));
        userRepos.save(user);

        return true;
    }

    public boolean authenticatedIsNotAnonymous() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth instanceof AnonymousAuthenticationToken)
            return false;

        return true;
    }

    public void updateUser(EUser upd_user) {
        userRepos.save(upd_user);
    }
}
