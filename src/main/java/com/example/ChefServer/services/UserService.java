package com.example.ChefServer.services;

import com.example.ChefServer.entities.*;
import com.example.ChefServer.repositories.*;
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

    public EUser getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth instanceof AnonymousAuthenticationToken) {
            return null;
        }
        return userRepos.findByUsername(auth.getName());
    }

    public boolean registerNewUser(EUser user) {
        if (userRepos.findByUsername(user.getUsername()) != null) {
            return false;
        }
        user.setActive(true);
        user.setRoles(Collections.singleton(EMRole.USER));
        userRepos.save(user);
        return true;
    }

    public boolean authenticatedIsNotAnonymous() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && !(auth instanceof AnonymousAuthenticationToken);
    }

    public void updateUser(EUser upd_user) {
        userRepos.save(upd_user);
    }
}