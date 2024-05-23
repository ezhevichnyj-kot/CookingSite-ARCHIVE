package com.example.ChefServer.entities;

import jakarta.persistence.GenerationType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;

@Entity(name = "UserRecipeConnector")
public class EConnectorUserRecipe {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long userid;
    private Long recipeid;

    public Long getId() {return id;}

    public Long getId_user() {return userid;}
    public void setId_user(Long userid) { this.userid = userid; }

    public Long getId_recipe() { return recipeid; }
    public void setId_recipe(Long recipeid) { this.recipeid = recipeid; }
}
