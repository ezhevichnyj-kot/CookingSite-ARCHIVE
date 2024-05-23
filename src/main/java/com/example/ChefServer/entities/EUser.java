package com.example.ChefServer.entities;

import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name="userdata")
public class EUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username;
    private String password;
    private boolean active; // TODO: strange field - is it necessary?
    private String photo_path;

    @ElementCollection(targetClass = EMRole.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<EMRole> roles;

    // functions below
    public EUser() {}

    public Long getId() {
        return id;
    }

    public String getUsername() {return username;}
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {return password;}
    public void setPassword(String password) { this.password = password; }

    public Boolean getActive() {return active;}
    public void setActive(boolean active) { this.active = active; }

    public Set<EMRole> getRoles() { return roles; }
    public void setRoles(Set<EMRole> roles){ this.roles = roles; }

    public String getPhoto_path() { return photo_path; }
    public void setPhoto_path(String photo_path) { this.photo_path = photo_path; }
}
